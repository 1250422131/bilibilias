package com.imcys.bilibilias.home.ui.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2022-12-27 16:13
 * @Description: 用户投稿类
 */
public class UserWorksBean implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"list":{"tlist":{"1":{"tid":1,"count":3,"name":"动画"},"160":{"tid":160,"count":23,"name":"生活"},"188":{"tid":188,"count":26,"name":"科技"},"36":{"tid":36,"count":47,"name":"知识"},"4":{"tid":4,"count":6,"name":"游戏"}},"vlist":[{"comment":13,"typeid":122,"play":425,"pic":"http://i2.hdslb.com/bfs/archive/f1746f70771901b20b28310afeb794c56ca3e06c.jpg","subtitle":"","description":"如果视频对你有用记得点赞+关注\n视频代码后面补充在评论区","copyright":"1","title":"【教程】PHP爬虫之正则表达式使用","review":0,"author":"萌新杰少","mid":351201307,"created":1665376769,"length":"17:41","video_review":0,"aid":773906332,"bvid":"BV1K14y177so","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":10,"typeid":122,"play":3563,"pic":"http://i0.hdslb.com/bfs/archive/5b24dc1787a75e6c718ecf2150a02b133c9ce82c.jpg","subtitle":"","description":"如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】Fiddler手机抓包注入","review":0,"author":"萌新杰少","mid":351201307,"created":1661844686,"length":"04:04","video_review":0,"aid":900062161,"bvid":"BV1xP4y1f77q","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":52,"typeid":122,"play":7086,"pic":"http://i1.hdslb.com/bfs/archive/f5b7baf09c44b2fae036c6137103aae4e7a881cf.jpg","subtitle":"","description":"此视频需要前置视频教学，如果感到困惑，请看看前一期间。\n如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】解决fiddler抓包手机断网问题","review":0,"author":"萌新杰少","mid":351201307,"created":1661150094,"length":"07:49","video_review":3,"aid":772301071,"bvid":"BV1w14y1x7Lh","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":32,"typeid":122,"play":4728,"pic":"http://i2.hdslb.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","subtitle":"","description":"如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】使用Fiddler代理手机抓包","review":0,"author":"萌新杰少","mid":351201307,"created":1661144767,"length":"04:05","video_review":3,"aid":259757310,"bvid":"BV1Fa411o7XL","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":22,"typeid":122,"play":1150,"pic":"http://i2.hdslb.com/bfs/archive/cee2fdc257db48d7204c94137e2f393430d3b039.jpg","subtitle":"","description":"本视频主要是实现了PHP去运行QR词库的功能，为了后期视频过渡。\n需要PHP和QR的基本基础。\n视频中类会在评论区置顶","copyright":"1","title":"【教程】通过PHP实现QR机器人词库","review":0,"author":"萌新杰少","mid":351201307,"created":1658733404,"length":"35:19","video_review":0,"aid":856257664,"bvid":"BV1BV4y177q6","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":551116,"title":"QR机器人","cover":"https://archive.biliimg.com/bfs/archive/2f10ac38e9888813e59ffeeb28ca7203e517ad2a.jpg","mid":351201307,"intro":"","sign_state":0,"attribute":140,"stat":{"season_id":551116,"view":212171,"danmaku":182,"reply":1246,"favorite":4882,"coin":2510,"share":1207,"like":5823},"ep_count":6,"first_aid":91189554,"ptime":1658733404,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":30,"typeid":122,"play":1334,"pic":"http://i2.hdslb.com/bfs/archive/9d66bd5392f94b7a20a20fb8f82cba0f78b4f949.jpg","subtitle":"","description":"QR机器人最后一期，后面我换机器人框架go-cqhttp了，如果还想继续学，了解QQ机器人，可以先准备一下环境。","copyright":"1","title":"【教程】QR机器人远程词库实现","review":0,"author":"萌新杰少","mid":351201307,"created":1657616185,"length":"04:13","video_review":0,"aid":385760378,"bvid":"BV1tZ4y1Y7yo","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":551116,"title":"QR机器人","cover":"https://archive.biliimg.com/bfs/archive/2f10ac38e9888813e59ffeeb28ca7203e517ad2a.jpg","mid":351201307,"intro":"","sign_state":0,"attribute":140,"stat":{"season_id":551116,"view":212171,"danmaku":182,"reply":1246,"favorite":4882,"coin":2510,"share":1207,"like":5823},"ep_count":6,"first_aid":91189554,"ptime":1658733404,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":60,"typeid":230,"play":971,"pic":"http://i2.hdslb.com/bfs/archive/749ee04eba53731e579da66ff4958674c1ea3c47.jpg","subtitle":"","description":"啊哈，好久不见，这是高考完的第一个视频，因为各种问题，我划水了1周多，现在开始恢复视频更新。\n这次也是请到了我的同学来帮忙配音（开心），大家有更好的意见欢迎在评论区告诉我们。\n视频中程序连接：\nhttps://wwu.lanzouy.com/iZrwT06q9x6b\n密码:mxjs\n配音：快乐的张憨憨","copyright":"1","title":"【教程】区域网HTTP传输文件","review":0,"author":"萌新杰少","mid":351201307,"created":1655799864,"length":"03:21","video_review":0,"aid":597748073,"bvid":"BV13B4y1q7Ar","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":36,"typeid":230,"play":6346,"pic":"http://i0.hdslb.com/bfs/archive/72bf88fed2b99a47eca55319fa0f4f68ad4dc014.jpg","subtitle":"","description":"如果视频对你有用，还请点个推荐。\n你们的支持和关注是我更新的最大动力！\n视频中程序下载地址：https://wwn.lanzouf.com/i1JoE02rot1e","copyright":"1","title":"【教程】阿里云盘本地挂载方法","review":0,"author":"萌新杰少","mid":351201307,"created":1649312333,"length":"04:43","video_review":3,"aid":810484605,"bvid":"BV1h34y1x7Jr","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":37,"typeid":230,"play":4470,"pic":"http://i0.hdslb.com/bfs/archive/6f02d6033743f690b88a3e849787bb09b8cad2e6.jpg","subtitle":"","description":"求支持，关注，高三正在努力更新当中！！！\n视频中程序下载地址：\nhttps://wwi.lanzouf.com/iGECl012p95i\n有更好的意见可以在评论区告诉我。","copyright":"1","title":"【分享】微信文件恢复程序","review":0,"author":"萌新杰少","mid":351201307,"created":1646550027,"length":"05:26","video_review":1,"aid":979568394,"bvid":"BV1644y1M7Vi","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":27,"typeid":230,"play":3501,"pic":"http://i1.hdslb.com/bfs/archive/2911af552aad21dbb004f2a1d67e1eb24c75dd6f.jpg","subtitle":"","description":"如果觉得视频帮助到你了，还请点个赞，投个币，不要白嫖啦！！！\n正在上传百度网盘，晚点补到评论区。\n如果可以还请关注，支持小杰！！！！\n下载信息\n链接：https://pan.baidu.com/s/1QuTXz5LWCdl0sfnw2ARPBw \n提取码：2233","copyright":"1","title":"【京阿尼桌宠】三大超有趣的桌面宠物","review":0,"author":"萌新杰少","mid":351201307,"created":1643536076,"length":"04:53","video_review":4,"aid":808683375,"bvid":"BV1S34y1276K","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":88,"typeid":230,"play":17207,"pic":"http://i0.hdslb.com/bfs/archive/5b0745cb7ad5e435a003b44232076a943c6cb1b1.jpg","subtitle":"","description":"如果APP对大家有用，还请大家点个推荐。\n今后会推荐更多实用类型的APP。\n软件下载地址：https://wwi.lanzoup.com/iza5Mz5p0ch\n也欢迎关注我，加入粉丝群，和更多的开发者们交流。","copyright":"1","title":"【推荐】强制屏幕转屏APP","review":0,"author":"萌新杰少","mid":351201307,"created":1642924497,"length":"04:57","video_review":9,"aid":338418697,"bvid":"BV1nR4y1u7hh","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":14,"typeid":122,"play":1507,"pic":"http://i0.hdslb.com/bfs/archive/e420a6f5018ebf0f339ff15f069155d2e65bf983.jpg","subtitle":"","description":"摆完了！参考网上的资料，用Python的OpenCV模块实现的视频转字符串合成的视频。\n原作：BV1xL4y1E7nT\n借用评论区的话\n2000年的人：真不敢想象未来人们会用OpenCV创作出多么惊艳与震撼的作品。\n2022年的人：开摆！","copyright":"1","title":"我用OpenCV摆烂了你的AE作业","review":0,"author":"萌新杰少","mid":351201307,"created":1641566566,"length":"01:13","video_review":1,"aid":850423304,"bvid":"BV1eL4y1t7e2","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":24,"typeid":230,"play":7949,"pic":"http://i2.hdslb.com/bfs/archive/a9f9e36b2a42df937859bda51d0f032f835bfa83.jpg","subtitle":"","description":"如果对大家有用，还请关注我支持一下。\n以后会更新更多实用APP和技能。\n下载地址：https://wwi.lanzoup.com/iUEluy88gqh","copyright":"1","title":"【干货】阿里云网盘电视版 可挂字幕","review":0,"author":"萌新杰少","mid":351201307,"created":1641035233,"length":"03:20","video_review":0,"aid":422781612,"bvid":"BV1V3411i7ec","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":10,"typeid":21,"play":246,"pic":"http://i1.hdslb.com/bfs/archive/8367c7d855821cc3c1ebb5385deb1e0c6e1f50ea.png","subtitle":"","description":"-","copyright":"1","title":"萌新杰少的2021全年回顾","review":0,"author":"萌新杰少","mid":351201307,"created":1640840328,"length":"01:06","video_review":0,"aid":892747246,"bvid":"BV1NP4y1n7Z7","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":46,"typeid":230,"play":6171,"pic":"http://i0.hdslb.com/bfs/archive/356fec7367128c60c150b5534d23f02ec2cb1324.jpg","subtitle":"","description":"awa，高三了感觉咕咕咕了好长时间，终于有时间更新视频了！！！\n做的比较赶，部分字幕和配音不一致，还请见谅。\n配音：快乐的张憨憨\nBGM：大鱼 钢琴版\n视频软件下载地址：https://wwi.lanzouo.com/itk1Nxizs6f","copyright":"1","title":"【推荐】手机上的免费格式工厂","review":0,"author":"萌新杰少","mid":351201307,"created":1639296395,"length":"04:26","video_review":1,"aid":764843201,"bvid":"BV16r4y1D7ir","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":32,"typeid":231,"play":2327,"pic":"http://i1.hdslb.com/bfs/archive/6cd73285d92cf1379d74c3457b8ea5724150f533.jpg","subtitle":"","description":"那个数组降序排列有点问题，大家自己研究下，加个判断，降序后.和..实际上是插在中间的。\n如果喜欢，还请点个关注，此系列看情况继续更新。","copyright":"1","title":"【教程】PHP动漫图片API制作教程|萌新杰少","review":0,"author":"萌新杰少","mid":351201307,"created":1637111261,"length":"09:18","video_review":2,"aid":336742598,"bvid":"BV1BR4y1t776","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":18,"typeid":231,"play":823,"pic":"http://i2.hdslb.com/bfs/archive/11eaefabc67129893db56d1c90e149e1da7efe2f.jpg","subtitle":"","description":"知道点安卓的皮毛，如果解释的有问题，务必指出。\n如果喜欢视频，还请关注一下我。","copyright":"1","title":"【教程】1024活动第5题逆向教程","review":0,"author":"萌新杰少","mid":351201307,"created":1635058472,"length":"15:37","video_review":3,"aid":208841146,"bvid":"BV1ph41187ZD","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":21,"typeid":230,"play":1727,"pic":"http://i1.hdslb.com/bfs/archive/0b7f39db9b71044b269b718b27fea9d2f8dae3d6.jpg","subtitle":"","description":"求推荐，求关注awa!!!\nAPP下载地址：https://wwi.lanzoui.com/ibW5tvb5ivg","copyright":"1","title":"【教程】用手机一键修复老照片","review":0,"author":"萌新杰少","mid":351201307,"created":1634134603,"length":"02:47","video_review":0,"aid":718540401,"bvid":"BV1VQ4y1D7xP","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":37,"typeid":17,"play":9917,"pic":"http://i1.hdslb.com/bfs/archive/d0450bdf5c6748814e230ec9a57a620c35a0b7a2.jpg","subtitle":"","description":"求关注，求三连\n如果想看汉化教程，可以在评论区告诉我\n软件原名:Photocrafter\n汉化版本下载:\nhttps://wwi.lanzoui.com/iNxTVv67cyf","copyright":"1","title":"【教程】我的世界一键导入图片","review":0,"author":"萌新杰少","mid":351201307,"created":1633851208,"length":"05:06","video_review":0,"aid":721013760,"bvid":"BV1YQ4y1B77f","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":16,"typeid":122,"play":3413,"pic":"http://i2.hdslb.com/bfs/archive/a51ed1915bb31e2717fe24c488a0a4e78c36f8fa.jpg","subtitle":"","description":"如果对你有用，还请点个赞，如果喜欢视频希望能关注一下我，以后更新更多好玩的东西。\nUP涨粉缓慢，求支持，awa。\n\ncustom-cursor官方地址\nhttps://custom-cursor.com/\ncustom-cursor客户端下载地址：\nhttps://wwi.lanzoui.com/ic160utkh6h","copyright":"1","title":"【教程】原神光标|PC端一键设置免费好看的光标","review":0,"author":"萌新杰少","mid":351201307,"created":1633249255,"length":"06:16","video_review":27,"aid":293276027,"bvid":"BV1Nf4y1J7hZ","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0}]},"page":{"pn":1,"ps":20,"count":105},"episodic_button":{"text":"播放全部","uri":"//www.bilibili.com/medialist/play/351201307?from=space"},"is_risk":false,"gaia_res_type":0,"gaia_data":null}
     */

    private int code;
    private String message;
    private int ttl;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * list : {"tlist":{"1":{"tid":1,"count":3,"name":"动画"},"160":{"tid":160,"count":23,"name":"生活"},"188":{"tid":188,"count":26,"name":"科技"},"36":{"tid":36,"count":47,"name":"知识"},"4":{"tid":4,"count":6,"name":"游戏"}},"vlist":[{"comment":13,"typeid":122,"play":425,"pic":"http://i2.hdslb.com/bfs/archive/f1746f70771901b20b28310afeb794c56ca3e06c.jpg","subtitle":"","description":"如果视频对你有用记得点赞+关注\n视频代码后面补充在评论区","copyright":"1","title":"【教程】PHP爬虫之正则表达式使用","review":0,"author":"萌新杰少","mid":351201307,"created":1665376769,"length":"17:41","video_review":0,"aid":773906332,"bvid":"BV1K14y177so","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":10,"typeid":122,"play":3563,"pic":"http://i0.hdslb.com/bfs/archive/5b24dc1787a75e6c718ecf2150a02b133c9ce82c.jpg","subtitle":"","description":"如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】Fiddler手机抓包注入","review":0,"author":"萌新杰少","mid":351201307,"created":1661844686,"length":"04:04","video_review":0,"aid":900062161,"bvid":"BV1xP4y1f77q","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":52,"typeid":122,"play":7086,"pic":"http://i1.hdslb.com/bfs/archive/f5b7baf09c44b2fae036c6137103aae4e7a881cf.jpg","subtitle":"","description":"此视频需要前置视频教学，如果感到困惑，请看看前一期间。\n如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】解决fiddler抓包手机断网问题","review":0,"author":"萌新杰少","mid":351201307,"created":1661150094,"length":"07:49","video_review":3,"aid":772301071,"bvid":"BV1w14y1x7Lh","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":32,"typeid":122,"play":4728,"pic":"http://i2.hdslb.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","subtitle":"","description":"如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】使用Fiddler代理手机抓包","review":0,"author":"萌新杰少","mid":351201307,"created":1661144767,"length":"04:05","video_review":3,"aid":259757310,"bvid":"BV1Fa411o7XL","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":22,"typeid":122,"play":1150,"pic":"http://i2.hdslb.com/bfs/archive/cee2fdc257db48d7204c94137e2f393430d3b039.jpg","subtitle":"","description":"本视频主要是实现了PHP去运行QR词库的功能，为了后期视频过渡。\n需要PHP和QR的基本基础。\n视频中类会在评论区置顶","copyright":"1","title":"【教程】通过PHP实现QR机器人词库","review":0,"author":"萌新杰少","mid":351201307,"created":1658733404,"length":"35:19","video_review":0,"aid":856257664,"bvid":"BV1BV4y177q6","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":551116,"title":"QR机器人","cover":"https://archive.biliimg.com/bfs/archive/2f10ac38e9888813e59ffeeb28ca7203e517ad2a.jpg","mid":351201307,"intro":"","sign_state":0,"attribute":140,"stat":{"season_id":551116,"view":212171,"danmaku":182,"reply":1246,"favorite":4882,"coin":2510,"share":1207,"like":5823},"ep_count":6,"first_aid":91189554,"ptime":1658733404,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":30,"typeid":122,"play":1334,"pic":"http://i2.hdslb.com/bfs/archive/9d66bd5392f94b7a20a20fb8f82cba0f78b4f949.jpg","subtitle":"","description":"QR机器人最后一期，后面我换机器人框架go-cqhttp了，如果还想继续学，了解QQ机器人，可以先准备一下环境。","copyright":"1","title":"【教程】QR机器人远程词库实现","review":0,"author":"萌新杰少","mid":351201307,"created":1657616185,"length":"04:13","video_review":0,"aid":385760378,"bvid":"BV1tZ4y1Y7yo","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":551116,"title":"QR机器人","cover":"https://archive.biliimg.com/bfs/archive/2f10ac38e9888813e59ffeeb28ca7203e517ad2a.jpg","mid":351201307,"intro":"","sign_state":0,"attribute":140,"stat":{"season_id":551116,"view":212171,"danmaku":182,"reply":1246,"favorite":4882,"coin":2510,"share":1207,"like":5823},"ep_count":6,"first_aid":91189554,"ptime":1658733404,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":60,"typeid":230,"play":971,"pic":"http://i2.hdslb.com/bfs/archive/749ee04eba53731e579da66ff4958674c1ea3c47.jpg","subtitle":"","description":"啊哈，好久不见，这是高考完的第一个视频，因为各种问题，我划水了1周多，现在开始恢复视频更新。\n这次也是请到了我的同学来帮忙配音（开心），大家有更好的意见欢迎在评论区告诉我们。\n视频中程序连接：\nhttps://wwu.lanzouy.com/iZrwT06q9x6b\n密码:mxjs\n配音：快乐的张憨憨","copyright":"1","title":"【教程】区域网HTTP传输文件","review":0,"author":"萌新杰少","mid":351201307,"created":1655799864,"length":"03:21","video_review":0,"aid":597748073,"bvid":"BV13B4y1q7Ar","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":36,"typeid":230,"play":6346,"pic":"http://i0.hdslb.com/bfs/archive/72bf88fed2b99a47eca55319fa0f4f68ad4dc014.jpg","subtitle":"","description":"如果视频对你有用，还请点个推荐。\n你们的支持和关注是我更新的最大动力！\n视频中程序下载地址：https://wwn.lanzouf.com/i1JoE02rot1e","copyright":"1","title":"【教程】阿里云盘本地挂载方法","review":0,"author":"萌新杰少","mid":351201307,"created":1649312333,"length":"04:43","video_review":3,"aid":810484605,"bvid":"BV1h34y1x7Jr","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":37,"typeid":230,"play":4470,"pic":"http://i0.hdslb.com/bfs/archive/6f02d6033743f690b88a3e849787bb09b8cad2e6.jpg","subtitle":"","description":"求支持，关注，高三正在努力更新当中！！！\n视频中程序下载地址：\nhttps://wwi.lanzouf.com/iGECl012p95i\n有更好的意见可以在评论区告诉我。","copyright":"1","title":"【分享】微信文件恢复程序","review":0,"author":"萌新杰少","mid":351201307,"created":1646550027,"length":"05:26","video_review":1,"aid":979568394,"bvid":"BV1644y1M7Vi","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":27,"typeid":230,"play":3501,"pic":"http://i1.hdslb.com/bfs/archive/2911af552aad21dbb004f2a1d67e1eb24c75dd6f.jpg","subtitle":"","description":"如果觉得视频帮助到你了，还请点个赞，投个币，不要白嫖啦！！！\n正在上传百度网盘，晚点补到评论区。\n如果可以还请关注，支持小杰！！！！\n下载信息\n链接：https://pan.baidu.com/s/1QuTXz5LWCdl0sfnw2ARPBw \n提取码：2233","copyright":"1","title":"【京阿尼桌宠】三大超有趣的桌面宠物","review":0,"author":"萌新杰少","mid":351201307,"created":1643536076,"length":"04:53","video_review":4,"aid":808683375,"bvid":"BV1S34y1276K","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":88,"typeid":230,"play":17207,"pic":"http://i0.hdslb.com/bfs/archive/5b0745cb7ad5e435a003b44232076a943c6cb1b1.jpg","subtitle":"","description":"如果APP对大家有用，还请大家点个推荐。\n今后会推荐更多实用类型的APP。\n软件下载地址：https://wwi.lanzoup.com/iza5Mz5p0ch\n也欢迎关注我，加入粉丝群，和更多的开发者们交流。","copyright":"1","title":"【推荐】强制屏幕转屏APP","review":0,"author":"萌新杰少","mid":351201307,"created":1642924497,"length":"04:57","video_review":9,"aid":338418697,"bvid":"BV1nR4y1u7hh","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":14,"typeid":122,"play":1507,"pic":"http://i0.hdslb.com/bfs/archive/e420a6f5018ebf0f339ff15f069155d2e65bf983.jpg","subtitle":"","description":"摆完了！参考网上的资料，用Python的OpenCV模块实现的视频转字符串合成的视频。\n原作：BV1xL4y1E7nT\n借用评论区的话\n2000年的人：真不敢想象未来人们会用OpenCV创作出多么惊艳与震撼的作品。\n2022年的人：开摆！","copyright":"1","title":"我用OpenCV摆烂了你的AE作业","review":0,"author":"萌新杰少","mid":351201307,"created":1641566566,"length":"01:13","video_review":1,"aid":850423304,"bvid":"BV1eL4y1t7e2","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":24,"typeid":230,"play":7949,"pic":"http://i2.hdslb.com/bfs/archive/a9f9e36b2a42df937859bda51d0f032f835bfa83.jpg","subtitle":"","description":"如果对大家有用，还请关注我支持一下。\n以后会更新更多实用APP和技能。\n下载地址：https://wwi.lanzoup.com/iUEluy88gqh","copyright":"1","title":"【干货】阿里云网盘电视版 可挂字幕","review":0,"author":"萌新杰少","mid":351201307,"created":1641035233,"length":"03:20","video_review":0,"aid":422781612,"bvid":"BV1V3411i7ec","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":10,"typeid":21,"play":246,"pic":"http://i1.hdslb.com/bfs/archive/8367c7d855821cc3c1ebb5385deb1e0c6e1f50ea.png","subtitle":"","description":"-","copyright":"1","title":"萌新杰少的2021全年回顾","review":0,"author":"萌新杰少","mid":351201307,"created":1640840328,"length":"01:06","video_review":0,"aid":892747246,"bvid":"BV1NP4y1n7Z7","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":46,"typeid":230,"play":6171,"pic":"http://i0.hdslb.com/bfs/archive/356fec7367128c60c150b5534d23f02ec2cb1324.jpg","subtitle":"","description":"awa，高三了感觉咕咕咕了好长时间，终于有时间更新视频了！！！\n做的比较赶，部分字幕和配音不一致，还请见谅。\n配音：快乐的张憨憨\nBGM：大鱼 钢琴版\n视频软件下载地址：https://wwi.lanzouo.com/itk1Nxizs6f","copyright":"1","title":"【推荐】手机上的免费格式工厂","review":0,"author":"萌新杰少","mid":351201307,"created":1639296395,"length":"04:26","video_review":1,"aid":764843201,"bvid":"BV16r4y1D7ir","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":32,"typeid":231,"play":2327,"pic":"http://i1.hdslb.com/bfs/archive/6cd73285d92cf1379d74c3457b8ea5724150f533.jpg","subtitle":"","description":"那个数组降序排列有点问题，大家自己研究下，加个判断，降序后.和..实际上是插在中间的。\n如果喜欢，还请点个关注，此系列看情况继续更新。","copyright":"1","title":"【教程】PHP动漫图片API制作教程|萌新杰少","review":0,"author":"萌新杰少","mid":351201307,"created":1637111261,"length":"09:18","video_review":2,"aid":336742598,"bvid":"BV1BR4y1t776","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":18,"typeid":231,"play":823,"pic":"http://i2.hdslb.com/bfs/archive/11eaefabc67129893db56d1c90e149e1da7efe2f.jpg","subtitle":"","description":"知道点安卓的皮毛，如果解释的有问题，务必指出。\n如果喜欢视频，还请关注一下我。","copyright":"1","title":"【教程】1024活动第5题逆向教程","review":0,"author":"萌新杰少","mid":351201307,"created":1635058472,"length":"15:37","video_review":3,"aid":208841146,"bvid":"BV1ph41187ZD","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":21,"typeid":230,"play":1727,"pic":"http://i1.hdslb.com/bfs/archive/0b7f39db9b71044b269b718b27fea9d2f8dae3d6.jpg","subtitle":"","description":"求推荐，求关注awa!!!\nAPP下载地址：https://wwi.lanzoui.com/ibW5tvb5ivg","copyright":"1","title":"【教程】用手机一键修复老照片","review":0,"author":"萌新杰少","mid":351201307,"created":1634134603,"length":"02:47","video_review":0,"aid":718540401,"bvid":"BV1VQ4y1D7xP","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":37,"typeid":17,"play":9917,"pic":"http://i1.hdslb.com/bfs/archive/d0450bdf5c6748814e230ec9a57a620c35a0b7a2.jpg","subtitle":"","description":"求关注，求三连\n如果想看汉化教程，可以在评论区告诉我\n软件原名:Photocrafter\n汉化版本下载:\nhttps://wwi.lanzoui.com/iNxTVv67cyf","copyright":"1","title":"【教程】我的世界一键导入图片","review":0,"author":"萌新杰少","mid":351201307,"created":1633851208,"length":"05:06","video_review":0,"aid":721013760,"bvid":"BV1YQ4y1B77f","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":16,"typeid":122,"play":3413,"pic":"http://i2.hdslb.com/bfs/archive/a51ed1915bb31e2717fe24c488a0a4e78c36f8fa.jpg","subtitle":"","description":"如果对你有用，还请点个赞，如果喜欢视频希望能关注一下我，以后更新更多好玩的东西。\nUP涨粉缓慢，求支持，awa。\n\ncustom-cursor官方地址\nhttps://custom-cursor.com/\ncustom-cursor客户端下载地址：\nhttps://wwi.lanzoui.com/ic160utkh6h","copyright":"1","title":"【教程】原神光标|PC端一键设置免费好看的光标","review":0,"author":"萌新杰少","mid":351201307,"created":1633249255,"length":"06:16","video_review":27,"aid":293276027,"bvid":"BV1Nf4y1J7hZ","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0}]}
         * page : {"pn":1,"ps":20,"count":105}
         * episodic_button : {"text":"播放全部","uri":"//www.bilibili.com/medialist/play/351201307?from=space"}
         * is_risk : false
         * gaia_res_type : 0
         * gaia_data : null
         */

        private ListBean list;
        private PageBean page;
        private EpisodicButtonBean episodic_button;
        private boolean is_risk;
        private int gaia_res_type;
        private Object gaia_data;

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public EpisodicButtonBean getEpisodic_button() {
            return episodic_button;
        }

        public void setEpisodic_button(EpisodicButtonBean episodic_button) {
            this.episodic_button = episodic_button;
        }

        public boolean isIs_risk() {
            return is_risk;
        }

        public void setIs_risk(boolean is_risk) {
            this.is_risk = is_risk;
        }

        public int getGaia_res_type() {
            return gaia_res_type;
        }

        public void setGaia_res_type(int gaia_res_type) {
            this.gaia_res_type = gaia_res_type;
        }

        public Object getGaia_data() {
            return gaia_data;
        }

        public void setGaia_data(Object gaia_data) {
            this.gaia_data = gaia_data;
        }

        public static class ListBean implements Serializable {
            /**
             * tlist : {"1":{"tid":1,"count":3,"name":"动画"},"160":{"tid":160,"count":23,"name":"生活"},"188":{"tid":188,"count":26,"name":"科技"},"36":{"tid":36,"count":47,"name":"知识"},"4":{"tid":4,"count":6,"name":"游戏"}}
             * vlist : [{"comment":13,"typeid":122,"play":425,"pic":"http://i2.hdslb.com/bfs/archive/f1746f70771901b20b28310afeb794c56ca3e06c.jpg","subtitle":"","description":"如果视频对你有用记得点赞+关注\n视频代码后面补充在评论区","copyright":"1","title":"【教程】PHP爬虫之正则表达式使用","review":0,"author":"萌新杰少","mid":351201307,"created":1665376769,"length":"17:41","video_review":0,"aid":773906332,"bvid":"BV1K14y177so","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":10,"typeid":122,"play":3563,"pic":"http://i0.hdslb.com/bfs/archive/5b24dc1787a75e6c718ecf2150a02b133c9ce82c.jpg","subtitle":"","description":"如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】Fiddler手机抓包注入","review":0,"author":"萌新杰少","mid":351201307,"created":1661844686,"length":"04:04","video_review":0,"aid":900062161,"bvid":"BV1xP4y1f77q","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":52,"typeid":122,"play":7086,"pic":"http://i1.hdslb.com/bfs/archive/f5b7baf09c44b2fae036c6137103aae4e7a881cf.jpg","subtitle":"","description":"此视频需要前置视频教学，如果感到困惑，请看看前一期间。\n如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】解决fiddler抓包手机断网问题","review":0,"author":"萌新杰少","mid":351201307,"created":1661150094,"length":"07:49","video_review":3,"aid":772301071,"bvid":"BV1w14y1x7Lh","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":32,"typeid":122,"play":4728,"pic":"http://i2.hdslb.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","subtitle":"","description":"如果对你有用，记得点赞+关注，你的支持就是我更新的动力。\n视频中Fiddler抓包软件下载地址：\nhttps://wwu.lanzouy.com/iVTmC0a09mhe","copyright":"1","title":"【教程】使用Fiddler代理手机抓包","review":0,"author":"萌新杰少","mid":351201307,"created":1661144767,"length":"04:05","video_review":3,"aid":259757310,"bvid":"BV1Fa411o7XL","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":657036,"title":"【抓包】fiddler抓包合集","cover":"https://archive.biliimg.com/bfs/archive/1cc96b7f7498c12e591e2aee0a02e7585c1a9b35.jpg","mid":351201307,"intro":"此合集主要讲解fiddler对手机代理抓包的相关事项。","sign_state":0,"attribute":132,"stat":{"season_id":657036,"view":15376,"danmaku":6,"reply":94,"favorite":537,"coin":136,"share":47,"like":387},"ep_count":3,"first_aid":259757310,"ptime":1661844686,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":22,"typeid":122,"play":1150,"pic":"http://i2.hdslb.com/bfs/archive/cee2fdc257db48d7204c94137e2f393430d3b039.jpg","subtitle":"","description":"本视频主要是实现了PHP去运行QR词库的功能，为了后期视频过渡。\n需要PHP和QR的基本基础。\n视频中类会在评论区置顶","copyright":"1","title":"【教程】通过PHP实现QR机器人词库","review":0,"author":"萌新杰少","mid":351201307,"created":1658733404,"length":"35:19","video_review":0,"aid":856257664,"bvid":"BV1BV4y177q6","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":551116,"title":"QR机器人","cover":"https://archive.biliimg.com/bfs/archive/2f10ac38e9888813e59ffeeb28ca7203e517ad2a.jpg","mid":351201307,"intro":"","sign_state":0,"attribute":140,"stat":{"season_id":551116,"view":212171,"danmaku":182,"reply":1246,"favorite":4882,"coin":2510,"share":1207,"like":5823},"ep_count":6,"first_aid":91189554,"ptime":1658733404,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":30,"typeid":122,"play":1334,"pic":"http://i2.hdslb.com/bfs/archive/9d66bd5392f94b7a20a20fb8f82cba0f78b4f949.jpg","subtitle":"","description":"QR机器人最后一期，后面我换机器人框架go-cqhttp了，如果还想继续学，了解QQ机器人，可以先准备一下环境。","copyright":"1","title":"【教程】QR机器人远程词库实现","review":0,"author":"萌新杰少","mid":351201307,"created":1657616185,"length":"04:13","video_review":0,"aid":385760378,"bvid":"BV1tZ4y1Y7yo","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":{"id":551116,"title":"QR机器人","cover":"https://archive.biliimg.com/bfs/archive/2f10ac38e9888813e59ffeeb28ca7203e517ad2a.jpg","mid":351201307,"intro":"","sign_state":0,"attribute":140,"stat":{"season_id":551116,"view":212171,"danmaku":182,"reply":1246,"favorite":4882,"coin":2510,"share":1207,"like":5823},"ep_count":6,"first_aid":91189554,"ptime":1658733404,"ep_num":0},"is_avoided":0,"attribute":0},{"comment":60,"typeid":230,"play":971,"pic":"http://i2.hdslb.com/bfs/archive/749ee04eba53731e579da66ff4958674c1ea3c47.jpg","subtitle":"","description":"啊哈，好久不见，这是高考完的第一个视频，因为各种问题，我划水了1周多，现在开始恢复视频更新。\n这次也是请到了我的同学来帮忙配音（开心），大家有更好的意见欢迎在评论区告诉我们。\n视频中程序连接：\nhttps://wwu.lanzouy.com/iZrwT06q9x6b\n密码:mxjs\n配音：快乐的张憨憨","copyright":"1","title":"【教程】区域网HTTP传输文件","review":0,"author":"萌新杰少","mid":351201307,"created":1655799864,"length":"03:21","video_review":0,"aid":597748073,"bvid":"BV13B4y1q7Ar","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":36,"typeid":230,"play":6346,"pic":"http://i0.hdslb.com/bfs/archive/72bf88fed2b99a47eca55319fa0f4f68ad4dc014.jpg","subtitle":"","description":"如果视频对你有用，还请点个推荐。\n你们的支持和关注是我更新的最大动力！\n视频中程序下载地址：https://wwn.lanzouf.com/i1JoE02rot1e","copyright":"1","title":"【教程】阿里云盘本地挂载方法","review":0,"author":"萌新杰少","mid":351201307,"created":1649312333,"length":"04:43","video_review":3,"aid":810484605,"bvid":"BV1h34y1x7Jr","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":37,"typeid":230,"play":4470,"pic":"http://i0.hdslb.com/bfs/archive/6f02d6033743f690b88a3e849787bb09b8cad2e6.jpg","subtitle":"","description":"求支持，关注，高三正在努力更新当中！！！\n视频中程序下载地址：\nhttps://wwi.lanzouf.com/iGECl012p95i\n有更好的意见可以在评论区告诉我。","copyright":"1","title":"【分享】微信文件恢复程序","review":0,"author":"萌新杰少","mid":351201307,"created":1646550027,"length":"05:26","video_review":1,"aid":979568394,"bvid":"BV1644y1M7Vi","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":27,"typeid":230,"play":3501,"pic":"http://i1.hdslb.com/bfs/archive/2911af552aad21dbb004f2a1d67e1eb24c75dd6f.jpg","subtitle":"","description":"如果觉得视频帮助到你了，还请点个赞，投个币，不要白嫖啦！！！\n正在上传百度网盘，晚点补到评论区。\n如果可以还请关注，支持小杰！！！！\n下载信息\n链接：https://pan.baidu.com/s/1QuTXz5LWCdl0sfnw2ARPBw \n提取码：2233","copyright":"1","title":"【京阿尼桌宠】三大超有趣的桌面宠物","review":0,"author":"萌新杰少","mid":351201307,"created":1643536076,"length":"04:53","video_review":4,"aid":808683375,"bvid":"BV1S34y1276K","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":88,"typeid":230,"play":17207,"pic":"http://i0.hdslb.com/bfs/archive/5b0745cb7ad5e435a003b44232076a943c6cb1b1.jpg","subtitle":"","description":"如果APP对大家有用，还请大家点个推荐。\n今后会推荐更多实用类型的APP。\n软件下载地址：https://wwi.lanzoup.com/iza5Mz5p0ch\n也欢迎关注我，加入粉丝群，和更多的开发者们交流。","copyright":"1","title":"【推荐】强制屏幕转屏APP","review":0,"author":"萌新杰少","mid":351201307,"created":1642924497,"length":"04:57","video_review":9,"aid":338418697,"bvid":"BV1nR4y1u7hh","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":14,"typeid":122,"play":1507,"pic":"http://i0.hdslb.com/bfs/archive/e420a6f5018ebf0f339ff15f069155d2e65bf983.jpg","subtitle":"","description":"摆完了！参考网上的资料，用Python的OpenCV模块实现的视频转字符串合成的视频。\n原作：BV1xL4y1E7nT\n借用评论区的话\n2000年的人：真不敢想象未来人们会用OpenCV创作出多么惊艳与震撼的作品。\n2022年的人：开摆！","copyright":"1","title":"我用OpenCV摆烂了你的AE作业","review":0,"author":"萌新杰少","mid":351201307,"created":1641566566,"length":"01:13","video_review":1,"aid":850423304,"bvid":"BV1eL4y1t7e2","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":24,"typeid":230,"play":7949,"pic":"http://i2.hdslb.com/bfs/archive/a9f9e36b2a42df937859bda51d0f032f835bfa83.jpg","subtitle":"","description":"如果对大家有用，还请关注我支持一下。\n以后会更新更多实用APP和技能。\n下载地址：https://wwi.lanzoup.com/iUEluy88gqh","copyright":"1","title":"【干货】阿里云网盘电视版 可挂字幕","review":0,"author":"萌新杰少","mid":351201307,"created":1641035233,"length":"03:20","video_review":0,"aid":422781612,"bvid":"BV1V3411i7ec","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":10,"typeid":21,"play":246,"pic":"http://i1.hdslb.com/bfs/archive/8367c7d855821cc3c1ebb5385deb1e0c6e1f50ea.png","subtitle":"","description":"-","copyright":"1","title":"萌新杰少的2021全年回顾","review":0,"author":"萌新杰少","mid":351201307,"created":1640840328,"length":"01:06","video_review":0,"aid":892747246,"bvid":"BV1NP4y1n7Z7","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":46,"typeid":230,"play":6171,"pic":"http://i0.hdslb.com/bfs/archive/356fec7367128c60c150b5534d23f02ec2cb1324.jpg","subtitle":"","description":"awa，高三了感觉咕咕咕了好长时间，终于有时间更新视频了！！！\n做的比较赶，部分字幕和配音不一致，还请见谅。\n配音：快乐的张憨憨\nBGM：大鱼 钢琴版\n视频软件下载地址：https://wwi.lanzouo.com/itk1Nxizs6f","copyright":"1","title":"【推荐】手机上的免费格式工厂","review":0,"author":"萌新杰少","mid":351201307,"created":1639296395,"length":"04:26","video_review":1,"aid":764843201,"bvid":"BV16r4y1D7ir","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":32,"typeid":231,"play":2327,"pic":"http://i1.hdslb.com/bfs/archive/6cd73285d92cf1379d74c3457b8ea5724150f533.jpg","subtitle":"","description":"那个数组降序排列有点问题，大家自己研究下，加个判断，降序后.和..实际上是插在中间的。\n如果喜欢，还请点个关注，此系列看情况继续更新。","copyright":"1","title":"【教程】PHP动漫图片API制作教程|萌新杰少","review":0,"author":"萌新杰少","mid":351201307,"created":1637111261,"length":"09:18","video_review":2,"aid":336742598,"bvid":"BV1BR4y1t776","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":18,"typeid":231,"play":823,"pic":"http://i2.hdslb.com/bfs/archive/11eaefabc67129893db56d1c90e149e1da7efe2f.jpg","subtitle":"","description":"知道点安卓的皮毛，如果解释的有问题，务必指出。\n如果喜欢视频，还请关注一下我。","copyright":"1","title":"【教程】1024活动第5题逆向教程","review":0,"author":"萌新杰少","mid":351201307,"created":1635058472,"length":"15:37","video_review":3,"aid":208841146,"bvid":"BV1ph41187ZD","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":21,"typeid":230,"play":1727,"pic":"http://i1.hdslb.com/bfs/archive/0b7f39db9b71044b269b718b27fea9d2f8dae3d6.jpg","subtitle":"","description":"求推荐，求关注awa!!!\nAPP下载地址：https://wwi.lanzoui.com/ibW5tvb5ivg","copyright":"1","title":"【教程】用手机一键修复老照片","review":0,"author":"萌新杰少","mid":351201307,"created":1634134603,"length":"02:47","video_review":0,"aid":718540401,"bvid":"BV1VQ4y1D7xP","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":37,"typeid":17,"play":9917,"pic":"http://i1.hdslb.com/bfs/archive/d0450bdf5c6748814e230ec9a57a620c35a0b7a2.jpg","subtitle":"","description":"求关注，求三连\n如果想看汉化教程，可以在评论区告诉我\n软件原名:Photocrafter\n汉化版本下载:\nhttps://wwi.lanzoui.com/iNxTVv67cyf","copyright":"1","title":"【教程】我的世界一键导入图片","review":0,"author":"萌新杰少","mid":351201307,"created":1633851208,"length":"05:06","video_review":0,"aid":721013760,"bvid":"BV1YQ4y1B77f","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0},{"comment":16,"typeid":122,"play":3413,"pic":"http://i2.hdslb.com/bfs/archive/a51ed1915bb31e2717fe24c488a0a4e78c36f8fa.jpg","subtitle":"","description":"如果对你有用，还请点个赞，如果喜欢视频希望能关注一下我，以后更新更多好玩的东西。\nUP涨粉缓慢，求支持，awa。\n\ncustom-cursor官方地址\nhttps://custom-cursor.com/\ncustom-cursor客户端下载地址：\nhttps://wwi.lanzoui.com/ic160utkh6h","copyright":"1","title":"【教程】原神光标|PC端一键设置免费好看的光标","review":0,"author":"萌新杰少","mid":351201307,"created":1633249255,"length":"06:16","video_review":27,"aid":293276027,"bvid":"BV1Nf4y1J7hZ","hide_click":false,"is_pay":0,"is_union_video":0,"is_steins_gate":0,"is_live_playback":0,"meta":null,"is_avoided":0,"attribute":0}]
             */

            private TlistBean tlist;
            private List<VlistBean> vlist;

            public TlistBean getTlist() {
                return tlist;
            }

            public void setTlist(TlistBean tlist) {
                this.tlist = tlist;
            }

            public List<VlistBean> getVlist() {
                return vlist;
            }

            public void setVlist(List<VlistBean> vlist) {
                this.vlist = vlist;
            }

            public static class TlistBean implements Serializable {
                /**
                 * 1 : {"tid":1,"count":3,"name":"动画"}
                 * 160 : {"tid":160,"count":23,"name":"生活"}
                 * 188 : {"tid":188,"count":26,"name":"科技"}
                 * 36 : {"tid":36,"count":47,"name":"知识"}
                 * 4 : {"tid":4,"count":6,"name":"游戏"}
                 */

                @SerializedName("1")
                private _$1Bean _$1;
                @SerializedName("160")
                private _$160Bean _$160;
                @SerializedName("188")
                private _$188Bean _$188;
                @SerializedName("36")
                private _$36Bean _$36;
                @SerializedName("4")
                private _$4Bean _$4;

                public _$1Bean get_$1() {
                    return _$1;
                }

                public void set_$1(_$1Bean _$1) {
                    this._$1 = _$1;
                }

                public _$160Bean get_$160() {
                    return _$160;
                }

                public void set_$160(_$160Bean _$160) {
                    this._$160 = _$160;
                }

                public _$188Bean get_$188() {
                    return _$188;
                }

                public void set_$188(_$188Bean _$188) {
                    this._$188 = _$188;
                }

                public _$36Bean get_$36() {
                    return _$36;
                }

                public void set_$36(_$36Bean _$36) {
                    this._$36 = _$36;
                }

                public _$4Bean get_$4() {
                    return _$4;
                }

                public void set_$4(_$4Bean _$4) {
                    this._$4 = _$4;
                }

                public static class _$1Bean implements Serializable {
                    /**
                     * tid : 1
                     * count : 3
                     * name : 动画
                     */

                    private int tid;
                    private int count;
                    private String name;

                    public int getTid() {
                        return tid;
                    }

                    public void setTid(int tid) {
                        this.tid = tid;
                    }

                    public int getCount() {
                        return count;
                    }

                    public void setCount(int count) {
                        this.count = count;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

                public static class _$160Bean implements Serializable {
                    /**
                     * tid : 160
                     * count : 23
                     * name : 生活
                     */

                    private int tid;
                    private int count;
                    private String name;

                    public int getTid() {
                        return tid;
                    }

                    public void setTid(int tid) {
                        this.tid = tid;
                    }

                    public int getCount() {
                        return count;
                    }

                    public void setCount(int count) {
                        this.count = count;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

                public static class _$188Bean implements Serializable {
                    /**
                     * tid : 188
                     * count : 26
                     * name : 科技
                     */

                    private int tid;
                    private int count;
                    private String name;

                    public int getTid() {
                        return tid;
                    }

                    public void setTid(int tid) {
                        this.tid = tid;
                    }

                    public int getCount() {
                        return count;
                    }

                    public void setCount(int count) {
                        this.count = count;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

                public static class _$36Bean implements Serializable {
                    /**
                     * tid : 36
                     * count : 47
                     * name : 知识
                     */

                    private int tid;
                    private int count;
                    private String name;

                    public int getTid() {
                        return tid;
                    }

                    public void setTid(int tid) {
                        this.tid = tid;
                    }

                    public int getCount() {
                        return count;
                    }

                    public void setCount(int count) {
                        this.count = count;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }

                public static class _$4Bean implements Serializable {
                    /**
                     * tid : 4
                     * count : 6
                     * name : 游戏
                     */

                    private int tid;
                    private int count;
                    private String name;

                    public int getTid() {
                        return tid;
                    }

                    public void setTid(int tid) {
                        this.tid = tid;
                    }

                    public int getCount() {
                        return count;
                    }

                    public void setCount(int count) {
                        this.count = count;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }

            public static class VlistBean implements Serializable {
                /**
                 * comment : 13
                 * typeid : 122
                 * play : 425
                 * pic : http://i2.hdslb.com/bfs/archive/f1746f70771901b20b28310afeb794c56ca3e06c.jpg
                 * subtitle :
                 * description : 如果视频对你有用记得点赞+关注
                 * 视频代码后面补充在评论区
                 * copyright : 1
                 * title : 【教程】PHP爬虫之正则表达式使用
                 * review : 0
                 * author : 萌新杰少
                 * mid : 351201307
                 * created : 1665376769
                 * length : 17:41
                 * video_review : 0
                 * aid : 773906332
                 * bvid : BV1K14y177so
                 * hide_click : false
                 * is_pay : 0
                 * is_union_video : 0
                 * is_steins_gate : 0
                 * is_live_playback : 0
                 * meta : null
                 * is_avoided : 0
                 * attribute : 0
                 */

                private int comment;
                private int typeid;
                private int play;
                private String pic;
                private String subtitle;
                private String description;
                private String copyright;
                private String title;
                private int review;
                private String author;
                private int mid;
                private int created;
                private String length;
                private int video_review;
                private int aid;
                private String bvid;
                private boolean hide_click;
                private int is_pay;
                private int is_union_video;
                private int is_steins_gate;
                private int is_live_playback;
                private Object meta;
                private int is_avoided;
                private int attribute;

                public int getComment() {
                    return comment;
                }

                public void setComment(int comment) {
                    this.comment = comment;
                }

                public int getTypeid() {
                    return typeid;
                }

                public void setTypeid(int typeid) {
                    this.typeid = typeid;
                }

                public int getPlay() {
                    return play;
                }

                public void setPlay(int play) {
                    this.play = play;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getSubtitle() {
                    return subtitle;
                }

                public void setSubtitle(String subtitle) {
                    this.subtitle = subtitle;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getCopyright() {
                    return copyright;
                }

                public void setCopyright(String copyright) {
                    this.copyright = copyright;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getReview() {
                    return review;
                }

                public void setReview(int review) {
                    this.review = review;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public int getMid() {
                    return mid;
                }

                public void setMid(int mid) {
                    this.mid = mid;
                }

                public int getCreated() {
                    return created;
                }

                public void setCreated(int created) {
                    this.created = created;
                }

                public String getLength() {
                    return length;
                }

                public void setLength(String length) {
                    this.length = length;
                }

                public int getVideo_review() {
                    return video_review;
                }

                public void setVideo_review(int video_review) {
                    this.video_review = video_review;
                }

                public int getAid() {
                    return aid;
                }

                public void setAid(int aid) {
                    this.aid = aid;
                }

                public String getBvid() {
                    return bvid;
                }

                public void setBvid(String bvid) {
                    this.bvid = bvid;
                }

                public boolean isHide_click() {
                    return hide_click;
                }

                public void setHide_click(boolean hide_click) {
                    this.hide_click = hide_click;
                }

                public int getIs_pay() {
                    return is_pay;
                }

                public void setIs_pay(int is_pay) {
                    this.is_pay = is_pay;
                }

                public int getIs_union_video() {
                    return is_union_video;
                }

                public void setIs_union_video(int is_union_video) {
                    this.is_union_video = is_union_video;
                }

                public int getIs_steins_gate() {
                    return is_steins_gate;
                }

                public void setIs_steins_gate(int is_steins_gate) {
                    this.is_steins_gate = is_steins_gate;
                }

                public int getIs_live_playback() {
                    return is_live_playback;
                }

                public void setIs_live_playback(int is_live_playback) {
                    this.is_live_playback = is_live_playback;
                }

                public Object getMeta() {
                    return meta;
                }

                public void setMeta(Object meta) {
                    this.meta = meta;
                }

                public int getIs_avoided() {
                    return is_avoided;
                }

                public void setIs_avoided(int is_avoided) {
                    this.is_avoided = is_avoided;
                }

                public int getAttribute() {
                    return attribute;
                }

                public void setAttribute(int attribute) {
                    this.attribute = attribute;
                }
            }
        }

        public static class PageBean implements Serializable {
            /**
             * pn : 1
             * ps : 20
             * count : 105
             */

            private int pn;
            private int ps;
            private int count;

            public int getPn() {
                return pn;
            }

            public void setPn(int pn) {
                this.pn = pn;
            }

            public int getPs() {
                return ps;
            }

            public void setPs(int ps) {
                this.ps = ps;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }

        public static class EpisodicButtonBean implements Serializable {
            /**
             * text : 播放全部
             * uri : //www.bilibili.com/medialist/play/351201307?from=space
             */

            private String text;
            private String uri;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }
        }
    }
}
