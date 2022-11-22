package com.imcys.bilibilias.danmaku;

import android.graphics.Color;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.util.Locale;

import master.flame.danmaku.danmaku.model.AlphaValue;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.Duration;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuFactory;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.android.AndroidFileSource;
import master.flame.danmaku.danmaku.util.DanmakuUtils;

/**
 * @author:imcys
 * @create: 2022-11-21 23:00
 * @Description:
 */

public class BiliDanmukuParser extends master.flame.danmaku.danmaku.parser.BaseDanmakuParser {

    static {
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
    }

    private float mDispScaleX;
    private float mDispScaleY;

    @Override
    public Danmakus parse() {

        if (mDataSource != null) {
            AndroidFileSource source = (AndroidFileSource) mDataSource;
            try {
                XMLReader xmlReader = XMLReaderFactory.createXMLReader();
                XmlContentHandler contentHandler = new BiliDanmukuParser.XmlContentHandler();
                xmlReader.setContentHandler(contentHandler);
                xmlReader.parse(new InputSource(source.data()));
                return contentHandler.getResult();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public class XmlContentHandler extends DefaultHandler {

        private static final String TRUE_STRING = "true";

        public Danmakus result = null;

        public BaseDanmaku item = null;

        public boolean completed = false;

        public int index = 0;

        public Danmakus getResult() {
            return result;
        }

        @Override
        public void startDocument() throws SAXException {
            result = new Danmakus();
        }

        @Override
        public void endDocument() throws SAXException {
            completed = true;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            String tagName = localName.length() != 0 ? localName : qName;
            tagName = tagName.toLowerCase(Locale.getDefault()).trim();
            if (tagName.equals("d")) {
                // <d p="23.826000213623,1,25,16777215,1422201084,0,057075e9,757076900">我从未见过如此厚颜无耻之猴</d>
                // 0:时间(弹幕出现时间)
                // 1:类型(1从左至右滚动弹幕|6从右至左滚动弹幕|5顶端固定弹幕|4底端固定弹幕|7高级弹幕|8脚本弹幕)
                // 2:字号
                // 3:颜色
                // 4:时间戳 ?
                // 5:弹幕池id
                // 6:用户hash
                // 7:弹幕id
                String pValue = attributes.getValue("p");
                // parse p value to danmaku
                String[] values = pValue.split(",");
                if (values.length > 0) {
                    long time = (long) (Float.parseFloat(values[0]) * 1000); // 出现时间
                    int type = Integer.parseInt(values[1]); // 弹幕类型
                    float textSize = Float.parseFloat(values[2]); // 字体大小
                    int color = Integer.parseInt(values[3]) | 0xFF000000; // 颜色
                    // int poolType = Integer.parseInt(values[5]); // 弹幕池类型（忽略
                    item = mContext.mDanmakuFactory.createDanmaku(type, mContext);
                    if (item != null) {
                        item.setTime(time);
                        item.textSize = textSize * (mDispDensity - 0.6f);
                        item.textColor = color;
                        item.textShadowColor = color <= Color.BLACK ? Color.WHITE : Color.BLACK;
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (item != null) {
                if (item.duration != null) {
                    String tagName = localName.length() != 0 ? localName : qName;
                    if (tagName.equalsIgnoreCase("d")) {
                        item.setTimer(mTimer);
                        result.addItem(item);
                    }
                }
                item = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)  {
            if (item != null) {
                DanmakuUtils.fillText(item, decodeXmlString(new String(ch, start, length)));
                item.index = index++;

                // initial specail danmaku data
                String text = String.valueOf(item.text).trim();
                if (item.getType() == BaseDanmaku.TYPE_SPECIAL && text.startsWith("[")
                        && text.endsWith("]")) {
                    //text = text.substring(1, text.length() - 1);
                    String[] textArr = null;//text.split(",", -1);
                    try {
                        JSONArray jsonArray = new JSONArray(text);
                        textArr = new String[jsonArray.length()];
                        for(int i=0;i<textArr.length;i++){
                            textArr[i] = jsonArray.getString(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (textArr == null || textArr.length < 5) {
                        item = null;
                        return;
                    }
                    item.text = textArr[4];
                    float beginX = Float.parseFloat(textArr[0]);
                    float beginY = Float.parseFloat(textArr[1]);
                    float endX = beginX;
                    float endY = beginY;
                    String[] alphaArr = textArr[2].split("-");
                    int beginAlpha = (int) (AlphaValue.MAX * Float.parseFloat(alphaArr[0]));
                    int endAlpha = beginAlpha;
                    if (alphaArr.length > 1) {
                        endAlpha = (int) (AlphaValue.MAX * Float.parseFloat(alphaArr[1]));
                    }
                    long alphaDuraion = (long) (Float.parseFloat(textArr[3]) * 1000);
                    long translationDuration = alphaDuraion;
                    long translationStartDelay = 0;
                    float rotateY = 0, rotateZ = 0;
                    if (textArr.length >= 7) {
                        rotateZ = Float.parseFloat(textArr[5]);
                        rotateY = Float.parseFloat(textArr[6]);
                    }
                    if (textArr.length >= 11) {
                        endX = Float.parseFloat(textArr[7]);
                        endY = Float.parseFloat(textArr[8]);
                        if(!"".equals(textArr[9])){
                            translationDuration = Integer.parseInt(textArr[9]);
                        }
                        if(!"".equals(textArr[10])){
                            translationStartDelay = (long) (Float.parseFloat(textArr[10]));
                        }
                    }
                    item.duration = new Duration(alphaDuraion);
                    item.rotationZ = rotateZ;
                    item.rotationY = rotateY;
                    mContext.mDanmakuFactory.fillTranslationData(item, beginX,
                            beginY, endX, endY, translationDuration, translationStartDelay, mDispScaleX, mDispScaleY);
                    mContext.mDanmakuFactory.fillAlphaData(item, beginAlpha, endAlpha, alphaDuraion);

                    if (textArr.length >= 12) {
                        // 是否有描边
                        if (!TextUtils.isEmpty(textArr[11]) && TRUE_STRING.equals(textArr[11])) {
                            item.textShadowColor = Color.TRANSPARENT;
                        }
                    }
                    if (textArr.length >= 13) {
                        //TODO 字体 textArr[12]
                    }
                    if (textArr.length >= 14) {
                        //TODO 是否有加速
                    }
                    if (textArr.length >= 15) {
                        // 路径数据
                        if (!"".equals(textArr[14])) {
                            String motionPathString = textArr[14].substring(1);
                            String[] pointStrArray = motionPathString.split("L");
                            if (pointStrArray != null && pointStrArray.length > 0) {
                                float[][] points = new float[pointStrArray.length][2];
                                for (int i = 0; i < pointStrArray.length; i++) {
                                    String[] pointArray = pointStrArray[i].split(",");
                                    points[i][0] = Float.parseFloat(pointArray[0]);
                                    points[i][1] = Float.parseFloat(pointArray[1]);
                                }
                                mContext.mDanmakuFactory.fillLinePathData(item, points, mDispScaleX,
                                        mDispScaleY);
                            }
                        }
                    }
                }

            }
        }

        private String decodeXmlString(String title) {
            if (title.contains("&amp;")) {
                title = title.replace("&amp;", "&");
            }
            if (title.contains("&quot;")) {
                title = title.replace("&quot;", "\"");
            }
            if (title.contains("&gt;")) {
                title = title.replace("&gt;", ">");
            }
            if (title.contains("&lt;")) {
                title = title.replace("&lt;", "<");
            }
            return title;
        }

    }

    @Override
    public BaseDanmakuParser setDisplayer(IDisplayer disp) {
        super.setDisplayer(disp);
        mDispScaleX = mDispWidth / DanmakuFactory.BILI_PLAYER_WIDTH;
        mDispScaleY = mDispHeight / DanmakuFactory.BILI_PLAYER_HEIGHT;
        return this;
    }
}
