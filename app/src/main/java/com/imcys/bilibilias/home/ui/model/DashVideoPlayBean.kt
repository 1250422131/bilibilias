package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;


/**
 * @author:imcys
 * @create: 2022-12-06 16:59
 * @Description: dash类型视频返回数据
 */

public class DashVideoPlayBean implements Serializable {

    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"from":"local","result":"suee","message":"","quality":64,"format":"flv720","timelength":176540,"accept_format":"hdflv2,flv,flv720,flv480,mp4","accept_description":["高清 1080P+","高清 1080P","高清 720P","清晰 480P","流畅 360P"],"accept_quality":[112,80,64,32,16],"video_codecid":7,"seek_param":"start","seek_type":"offset","dash":{"duration":177,"minBufferTime":1.5,"min_buffer_time":1.5,"video":[{"id":80,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f245043243ea8369e3b572c1539af306&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=125095&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f245043243ea8369e3b572c1539af306&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=125095&logo=A0000800","backupUrl":["https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=coso1bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=21c4a20844f68c6ab4adf4819accc28e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000","https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=cosbv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=0a5bdaf5ce8d739948524a63733716cd&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000"],"backup_url":["https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=coso1bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=21c4a20844f68c6ab4adf4819accc28e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000","https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=cosbv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=0a5bdaf5ce8d739948524a63733716cd&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000"],"bandwidth":998309,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.640032","width":1432,"height":1080,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-991","indexRange":"992-1443"},"segment_base":{"initialization":"0-991","index_range":"992-1443"},"codecid":7},{"id":80,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6edc3ad91ada16ebe23d8e69a81a956e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=84073&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6edc3ad91ada16ebe23d8e69a81a956e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=84073&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f7bd03cc588b024861f415eeebaebd57&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=5af1dd9d262d83b58f7f16f16b235c04&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f7bd03cc588b024861f415eeebaebd57&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=5af1dd9d262d83b58f7f16f16b235c04&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000"],"bandwidth":670940,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":1432,"height":1080,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":64,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=679ed5fb5d454fe52c12838703ad546a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=45278&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=679ed5fb5d454fe52c12838703ad546a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=45278&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c08aee6828520e15a3458c4be07356e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3575533c79f2a247edebf5c32fb1755e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c08aee6828520e15a3458c4be07356e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3575533c79f2a247edebf5c32fb1755e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000"],"bandwidth":361342,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001F","width":954,"height":720,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-990","indexRange":"991-1442"},"segment_base":{"initialization":"0-990","index_range":"991-1442"},"codecid":7},{"id":64,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c8b2df8605cdebe7f3e1f33f0a7151f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=28350&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c8b2df8605cdebe7f3e1f33f0a7151f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=28350&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=39596589d9f614613f48671f583e85b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e85f27f65728632cafd62b425fd36479&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=39596589d9f614613f48671f583e85b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e85f27f65728632cafd62b425fd36479&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000"],"bandwidth":226245,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":954,"height":720,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":32,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6608fe7e56e24f1827500fa56f21af82&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=21091&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6608fe7e56e24f1827500fa56f21af82&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=21091&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=52f95ab0923b3e888b495b53e569bbf6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4a756726ad71c48545ffaf42701f0ac8&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=52f95ab0923b3e888b495b53e569bbf6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4a756726ad71c48545ffaf42701f0ac8&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000"],"bandwidth":168318,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001E","width":636,"height":480,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-990","indexRange":"991-1442"},"segment_base":{"initialization":"0-990","index_range":"991-1442"},"codecid":7},{"id":32,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=dbb2b1528658cb8548f135e938af7543&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16145&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=dbb2b1528658cb8548f135e938af7543&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16145&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4e4366d2dd89aaa10f94d1ee62db2f4a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=86da519e9e922a1fab473fa0fa78a0c6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4e4366d2dd89aaa10f94d1ee62db2f4a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=86da519e9e922a1fab473fa0fa78a0c6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000"],"bandwidth":128844,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":636,"height":480,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":16,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=8d155b2083ce13104138d86400efa66f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=38786&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=8d155b2083ce13104138d86400efa66f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=38786&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3937d72421f5c39fc54bb6366f8386c4&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e672feadb82157d6cd1050175130fdc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3937d72421f5c39fc54bb6366f8386c4&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e672feadb82157d6cd1050175130fdc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000"],"bandwidth":309529,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":476,"height":360,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1055","indexRange":"1056-1507"},"segment_base":{"initialization":"0-1055","index_range":"1056-1507"},"codecid":12},{"id":16,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6bcb70e6968af71ca92cdb2b34dcb7c1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=41278&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6bcb70e6968af71ca92cdb2b34dcb7c1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=41278&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=2a235a889e5a5811e5489d1912c19b40&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=7ac074696650f57e8ed360b1afa7ce4c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=2a235a889e5a5811e5489d1912c19b40&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=7ac074696650f57e8ed360b1afa7ce4c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000"],"bandwidth":329419,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001E","width":476,"height":360,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-997","indexRange":"998-1449"},"segment_base":{"initialization":"0-997","index_range":"998-1449"},"codecid":7}],"audio":[{"id":30280,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=eea3841d3665bc56bc68b18082a4fbc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=eea3841d3665bc56bc68b18082a4fbc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"bandwidth":129646,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-907","indexRange":"908-1371"},"segment_base":{"initialization":"0-907","index_range":"908-1371"},"codecid":0},{"id":30216,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6627b2ae307f54984a360f164f05ea24&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=8432&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6627b2ae307f54984a360f164f05ea24&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=8432&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=cc8f38440cf36ce062e75b88a6a06282&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=df04a8b38fa10ed1308630f6b141132b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=cc8f38440cf36ce062e75b88a6a06282&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=df04a8b38fa10ed1308630f6b141132b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000"],"bandwidth":67253,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-940","indexRange":"941-1404"},"segment_base":{"initialization":"0-940","index_range":"941-1404"},"codecid":0},{"id":30232,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=53ce0ee4cfc1a3c33773eba31b19b69e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=53ce0ee4cfc1a3c33773eba31b19b69e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"bandwidth":129646,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-907","indexRange":"908-1371"},"segment_base":{"initialization":"0-907","index_range":"908-1371"},"codecid":0}],"dolby":{"type":0,"audio":null},"flac":null},"support_formats":[{"quality":112,"format":"hdflv2","new_description":"1080P 高码率","display_desc":"1080P","superscript":"高码率","codecs":["avc1.640032","hev1.1.6.L120.90"]},{"quality":80,"format":"flv","new_description":"1080P 高清","display_desc":"1080P","superscript":"","codecs":["avc1.640032","hev1.1.6.L120.90"]},{"quality":64,"format":"flv720","new_description":"720P 高清","display_desc":"720P","superscript":"","codecs":["avc1.64001F","hev1.1.6.L120.90"]},{"quality":32,"format":"flv480","new_description":"480P 清晰","display_desc":"480P","superscript":"","codecs":["avc1.64001E","hev1.1.6.L120.90"]},{"quality":16,"format":"mp4","new_description":"360P 流畅","display_desc":"360P","superscript":"","codecs":["avc1.64001E","hev1.1.6.L120.90"]}],"high_format":null,"last_play_time":103000,"last_play_cid":553099035}
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
         * from : local
         * result : suee
         * message :
         * quality : 64
         * format : flv720
         * timelength : 176540
         * accept_format : hdflv2,flv,flv720,flv480,mp4
         * accept_description : ["高清 1080P+","高清 1080P","高清 720P","清晰 480P","流畅 360P"]
         * accept_quality : [112,80,64,32,16]
         * video_codecid : 7
         * seek_param : start
         * seek_type : offset
         * dash : {"duration":177,"minBufferTime":1.5,"min_buffer_time":1.5,"video":[{"id":80,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f245043243ea8369e3b572c1539af306&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=125095&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f245043243ea8369e3b572c1539af306&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=125095&logo=A0000800","backupUrl":["https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=coso1bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=21c4a20844f68c6ab4adf4819accc28e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000","https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=cosbv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=0a5bdaf5ce8d739948524a63733716cd&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000"],"backup_url":["https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=coso1bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=21c4a20844f68c6ab4adf4819accc28e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000","https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=cosbv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=0a5bdaf5ce8d739948524a63733716cd&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000"],"bandwidth":998309,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.640032","width":1432,"height":1080,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-991","indexRange":"992-1443"},"segment_base":{"initialization":"0-991","index_range":"992-1443"},"codecid":7},{"id":80,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6edc3ad91ada16ebe23d8e69a81a956e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=84073&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6edc3ad91ada16ebe23d8e69a81a956e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=84073&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f7bd03cc588b024861f415eeebaebd57&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=5af1dd9d262d83b58f7f16f16b235c04&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f7bd03cc588b024861f415eeebaebd57&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=5af1dd9d262d83b58f7f16f16b235c04&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000"],"bandwidth":670940,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":1432,"height":1080,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":64,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=679ed5fb5d454fe52c12838703ad546a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=45278&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=679ed5fb5d454fe52c12838703ad546a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=45278&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c08aee6828520e15a3458c4be07356e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3575533c79f2a247edebf5c32fb1755e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c08aee6828520e15a3458c4be07356e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3575533c79f2a247edebf5c32fb1755e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000"],"bandwidth":361342,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001F","width":954,"height":720,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-990","indexRange":"991-1442"},"segment_base":{"initialization":"0-990","index_range":"991-1442"},"codecid":7},{"id":64,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c8b2df8605cdebe7f3e1f33f0a7151f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=28350&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c8b2df8605cdebe7f3e1f33f0a7151f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=28350&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=39596589d9f614613f48671f583e85b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e85f27f65728632cafd62b425fd36479&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=39596589d9f614613f48671f583e85b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e85f27f65728632cafd62b425fd36479&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000"],"bandwidth":226245,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":954,"height":720,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":32,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6608fe7e56e24f1827500fa56f21af82&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=21091&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6608fe7e56e24f1827500fa56f21af82&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=21091&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=52f95ab0923b3e888b495b53e569bbf6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4a756726ad71c48545ffaf42701f0ac8&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=52f95ab0923b3e888b495b53e569bbf6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4a756726ad71c48545ffaf42701f0ac8&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000"],"bandwidth":168318,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001E","width":636,"height":480,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-990","indexRange":"991-1442"},"segment_base":{"initialization":"0-990","index_range":"991-1442"},"codecid":7},{"id":32,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=dbb2b1528658cb8548f135e938af7543&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16145&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=dbb2b1528658cb8548f135e938af7543&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16145&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4e4366d2dd89aaa10f94d1ee62db2f4a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=86da519e9e922a1fab473fa0fa78a0c6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4e4366d2dd89aaa10f94d1ee62db2f4a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=86da519e9e922a1fab473fa0fa78a0c6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000"],"bandwidth":128844,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":636,"height":480,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":16,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=8d155b2083ce13104138d86400efa66f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=38786&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=8d155b2083ce13104138d86400efa66f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=38786&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3937d72421f5c39fc54bb6366f8386c4&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e672feadb82157d6cd1050175130fdc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3937d72421f5c39fc54bb6366f8386c4&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e672feadb82157d6cd1050175130fdc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000"],"bandwidth":309529,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":476,"height":360,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1055","indexRange":"1056-1507"},"segment_base":{"initialization":"0-1055","index_range":"1056-1507"},"codecid":12},{"id":16,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6bcb70e6968af71ca92cdb2b34dcb7c1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=41278&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6bcb70e6968af71ca92cdb2b34dcb7c1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=41278&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=2a235a889e5a5811e5489d1912c19b40&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=7ac074696650f57e8ed360b1afa7ce4c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=2a235a889e5a5811e5489d1912c19b40&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=7ac074696650f57e8ed360b1afa7ce4c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000"],"bandwidth":329419,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001E","width":476,"height":360,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-997","indexRange":"998-1449"},"segment_base":{"initialization":"0-997","index_range":"998-1449"},"codecid":7}],"audio":[{"id":30280,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=eea3841d3665bc56bc68b18082a4fbc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=eea3841d3665bc56bc68b18082a4fbc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"bandwidth":129646,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-907","indexRange":"908-1371"},"segment_base":{"initialization":"0-907","index_range":"908-1371"},"codecid":0},{"id":30216,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6627b2ae307f54984a360f164f05ea24&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=8432&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6627b2ae307f54984a360f164f05ea24&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=8432&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=cc8f38440cf36ce062e75b88a6a06282&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=df04a8b38fa10ed1308630f6b141132b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=cc8f38440cf36ce062e75b88a6a06282&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=df04a8b38fa10ed1308630f6b141132b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000"],"bandwidth":67253,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-940","indexRange":"941-1404"},"segment_base":{"initialization":"0-940","index_range":"941-1404"},"codecid":0},{"id":30232,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=53ce0ee4cfc1a3c33773eba31b19b69e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=53ce0ee4cfc1a3c33773eba31b19b69e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"bandwidth":129646,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-907","indexRange":"908-1371"},"segment_base":{"initialization":"0-907","index_range":"908-1371"},"codecid":0}],"dolby":{"type":0,"audio":null},"flac":null}
         * support_formats : [{"quality":112,"format":"hdflv2","new_description":"1080P 高码率","display_desc":"1080P","superscript":"高码率","codecs":["avc1.640032","hev1.1.6.L120.90"]},{"quality":80,"format":"flv","new_description":"1080P 高清","display_desc":"1080P","superscript":"","codecs":["avc1.640032","hev1.1.6.L120.90"]},{"quality":64,"format":"flv720","new_description":"720P 高清","display_desc":"720P","superscript":"","codecs":["avc1.64001F","hev1.1.6.L120.90"]},{"quality":32,"format":"flv480","new_description":"480P 清晰","display_desc":"480P","superscript":"","codecs":["avc1.64001E","hev1.1.6.L120.90"]},{"quality":16,"format":"mp4","new_description":"360P 流畅","display_desc":"360P","superscript":"","codecs":["avc1.64001E","hev1.1.6.L120.90"]}]
         * high_format : null
         * last_play_time : 103000
         * last_play_cid : 553099035
         */

        private String from;
        private String result;
        private String message;
        private int quality;
        private String format;
        private int timelength;
        private String accept_format;
        private int video_codecid;
        private String seek_param;
        private String seek_type;
        private DashBean dash;
        private Object high_format;
        private int last_play_time;
        private int last_play_cid;
        private List<String> accept_description;
        private List<Integer> accept_quality;
        private List<SupportFormatsBean> support_formats;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getQuality() {
            return quality;
        }

        public void setQuality(int quality) {
            this.quality = quality;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public int getTimelength() {
            return timelength;
        }

        public void setTimelength(int timelength) {
            this.timelength = timelength;
        }

        public String getAccept_format() {
            return accept_format;
        }

        public void setAccept_format(String accept_format) {
            this.accept_format = accept_format;
        }

        public int getVideo_codecid() {
            return video_codecid;
        }

        public void setVideo_codecid(int video_codecid) {
            this.video_codecid = video_codecid;
        }

        public String getSeek_param() {
            return seek_param;
        }

        public void setSeek_param(String seek_param) {
            this.seek_param = seek_param;
        }

        public String getSeek_type() {
            return seek_type;
        }

        public void setSeek_type(String seek_type) {
            this.seek_type = seek_type;
        }

        public DashBean getDash() {
            return dash;
        }

        public void setDash(DashBean dash) {
            this.dash = dash;
        }

        public Object getHigh_format() {
            return high_format;
        }

        public void setHigh_format(Object high_format) {
            this.high_format = high_format;
        }

        public int getLast_play_time() {
            return last_play_time;
        }

        public void setLast_play_time(int last_play_time) {
            this.last_play_time = last_play_time;
        }

        public int getLast_play_cid() {
            return last_play_cid;
        }

        public void setLast_play_cid(int last_play_cid) {
            this.last_play_cid = last_play_cid;
        }

        public List<String> getAccept_description() {
            return accept_description;
        }

        public void setAccept_description(List<String> accept_description) {
            this.accept_description = accept_description;
        }

        public List<Integer> getAccept_quality() {
            return accept_quality;
        }

        public void setAccept_quality(List<Integer> accept_quality) {
            this.accept_quality = accept_quality;
        }

        public List<SupportFormatsBean> getSupport_formats() {
            return support_formats;
        }

        public void setSupport_formats(List<SupportFormatsBean> support_formats) {
            this.support_formats = support_formats;
        }

        public static class DashBean implements Serializable {
            /**
             * duration : 177
             * minBufferTime : 1.5
             * min_buffer_time : 1.5
             * video : [{"id":80,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f245043243ea8369e3b572c1539af306&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=125095&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f245043243ea8369e3b572c1539af306&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=125095&logo=A0000800","backupUrl":["https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=coso1bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=21c4a20844f68c6ab4adf4819accc28e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000","https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=cosbv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=0a5bdaf5ce8d739948524a63733716cd&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000"],"backup_url":["https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=coso1bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=21c4a20844f68c6ab4adf4819accc28e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000","https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=cosbv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=0a5bdaf5ce8d739948524a63733716cd&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000"],"bandwidth":998309,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.640032","width":1432,"height":1080,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-991","indexRange":"992-1443"},"segment_base":{"initialization":"0-991","index_range":"992-1443"},"codecid":7},{"id":80,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6edc3ad91ada16ebe23d8e69a81a956e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=84073&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6edc3ad91ada16ebe23d8e69a81a956e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=84073&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f7bd03cc588b024861f415eeebaebd57&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=5af1dd9d262d83b58f7f16f16b235c04&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f7bd03cc588b024861f415eeebaebd57&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30077.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=5af1dd9d262d83b58f7f16f16b235c04&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=84073&logo=40000000"],"bandwidth":670940,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":1432,"height":1080,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":64,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=679ed5fb5d454fe52c12838703ad546a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=45278&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=679ed5fb5d454fe52c12838703ad546a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=45278&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c08aee6828520e15a3458c4be07356e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3575533c79f2a247edebf5c32fb1755e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c08aee6828520e15a3458c4be07356e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30064.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3575533c79f2a247edebf5c32fb1755e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=45278&logo=40000000"],"bandwidth":361342,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001F","width":954,"height":720,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-990","indexRange":"991-1442"},"segment_base":{"initialization":"0-990","index_range":"991-1442"},"codecid":7},{"id":64,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c8b2df8605cdebe7f3e1f33f0a7151f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=28350&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1c8b2df8605cdebe7f3e1f33f0a7151f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=28350&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=39596589d9f614613f48671f583e85b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e85f27f65728632cafd62b425fd36479&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=39596589d9f614613f48671f583e85b1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30066.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e85f27f65728632cafd62b425fd36479&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=28350&logo=40000000"],"bandwidth":226245,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":954,"height":720,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":32,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6608fe7e56e24f1827500fa56f21af82&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=21091&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6608fe7e56e24f1827500fa56f21af82&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=21091&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=52f95ab0923b3e888b495b53e569bbf6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4a756726ad71c48545ffaf42701f0ac8&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=52f95ab0923b3e888b495b53e569bbf6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4a756726ad71c48545ffaf42701f0ac8&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=21091&logo=40000000"],"bandwidth":168318,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001E","width":636,"height":480,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-990","indexRange":"991-1442"},"segment_base":{"initialization":"0-990","index_range":"991-1442"},"codecid":7},{"id":32,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=dbb2b1528658cb8548f135e938af7543&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16145&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=dbb2b1528658cb8548f135e938af7543&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16145&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4e4366d2dd89aaa10f94d1ee62db2f4a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=86da519e9e922a1fab473fa0fa78a0c6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=4e4366d2dd89aaa10f94d1ee62db2f4a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30033.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=86da519e9e922a1fab473fa0fa78a0c6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16145&logo=40000000"],"bandwidth":128844,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":636,"height":480,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1154","indexRange":"1155-1606"},"segment_base":{"initialization":"0-1154","index_range":"1155-1606"},"codecid":12},{"id":16,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=8d155b2083ce13104138d86400efa66f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=38786&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=8d155b2083ce13104138d86400efa66f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=38786&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3937d72421f5c39fc54bb6366f8386c4&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e672feadb82157d6cd1050175130fdc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=3937d72421f5c39fc54bb6366f8386c4&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30011.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=e672feadb82157d6cd1050175130fdc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=38786&logo=40000000"],"bandwidth":309529,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"hev1.1.6.L120.90","width":476,"height":360,"frameRate":"30.303","frame_rate":"30.303","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-1055","indexRange":"1056-1507"},"segment_base":{"initialization":"0-1055","index_range":"1056-1507"},"codecid":12},{"id":16,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6bcb70e6968af71ca92cdb2b34dcb7c1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=41278&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6bcb70e6968af71ca92cdb2b34dcb7c1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=41278&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=2a235a889e5a5811e5489d1912c19b40&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=7ac074696650f57e8ed360b1afa7ce4c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=2a235a889e5a5811e5489d1912c19b40&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=7ac074696650f57e8ed360b1afa7ce4c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=41278&logo=40000000"],"bandwidth":329419,"mimeType":"video/mp4","mime_type":"video/mp4","codecs":"avc1.64001E","width":476,"height":360,"frameRate":"29.412","frame_rate":"29.412","sar":"1:1","startWithSap":1,"start_with_sap":1,"SegmentBase":{"Initialization":"0-997","indexRange":"998-1449"},"segment_base":{"initialization":"0-997","index_range":"998-1449"},"codecid":7}]
             * audio : [{"id":30280,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=eea3841d3665bc56bc68b18082a4fbc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=eea3841d3665bc56bc68b18082a4fbc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"bandwidth":129646,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-907","indexRange":"908-1371"},"segment_base":{"initialization":"0-907","index_range":"908-1371"},"codecid":0},{"id":30216,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6627b2ae307f54984a360f164f05ea24&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=8432&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=6627b2ae307f54984a360f164f05ea24&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=8432&logo=A0000800","backupUrl":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=cc8f38440cf36ce062e75b88a6a06282&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=df04a8b38fa10ed1308630f6b141132b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000"],"backup_url":["https://upos-sz-estgoss02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=upos&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=cc8f38440cf36ce062e75b88a6a06282&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=ali02bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=df04a8b38fa10ed1308630f6b141132b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=8432&logo=40000000"],"bandwidth":67253,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-940","indexRange":"941-1404"},"segment_base":{"initialization":"0-940","index_range":"941-1404"},"codecid":0},{"id":30232,"baseUrl":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=53ce0ee4cfc1a3c33773eba31b19b69e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","base_url":"https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=53ce0ee4cfc1a3c33773eba31b19b69e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=1337b86bfa857d95be43a15bc13d0c0b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"],"bandwidth":129646,"mimeType":"audio/mp4","mime_type":"audio/mp4","codecs":"mp4a.40.2","width":0,"height":0,"frameRate":"","frame_rate":"","sar":"","startWithSap":0,"start_with_sap":0,"SegmentBase":{"Initialization":"0-907","indexRange":"908-1371"},"segment_base":{"initialization":"0-907","index_range":"908-1371"},"codecid":0}]
             * dolby : {"type":0,"audio":null}
             * flac : null
             */

            private int duration;
            private double minBufferTime;
            private double min_buffer_time;
            private DolbyBean dolby;
            private Object flac;
            private List<VideoBean> video;
            private List<AudioBean> audio;

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public double getMinBufferTime() {
                return minBufferTime;
            }

            public void setMinBufferTime(double minBufferTime) {
                this.minBufferTime = minBufferTime;
            }

            public double getMin_buffer_time() {
                return min_buffer_time;
            }

            public void setMin_buffer_time(double min_buffer_time) {
                this.min_buffer_time = min_buffer_time;
            }

            public DolbyBean getDolby() {
                return dolby;
            }

            public void setDolby(DolbyBean dolby) {
                this.dolby = dolby;
            }

            public Object getFlac() {
                return flac;
            }

            public void setFlac(Object flac) {
                this.flac = flac;
            }

            public List<VideoBean> getVideo() {
                return video;
            }

            public void setVideo(List<VideoBean> video) {
                this.video = video;
            }

            public List<AudioBean> getAudio() {
                return audio;
            }

            public void setAudio(List<AudioBean> audio) {
                this.audio = audio;
            }

            public static class DolbyBean implements Serializable {
                /**
                 * type : 0
                 * audio : null
                 */

                private int type;
                private Object audio;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public Object getAudio() {
                    return audio;
                }

                public void setAudio(Object audio) {
                    this.audio = audio;
                }
            }

            public static class VideoBean implements Serializable {
                /**
                 * id : 80
                 * baseUrl : https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f245043243ea8369e3b572c1539af306&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=125095&logo=A0000800
                 * base_url : https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=f245043243ea8369e3b572c1539af306&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=125095&logo=A0000800
                 * backupUrl : ["https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=coso1bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=21c4a20844f68c6ab4adf4819accc28e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000","https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=cosbv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=0a5bdaf5ce8d739948524a63733716cd&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000"]
                 * backup_url : ["https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=coso1bv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=21c4a20844f68c6ab4adf4819accc28e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000","https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=cosbv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=0a5bdaf5ce8d739948524a63733716cd&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=125095&logo=40000000"]
                 * bandwidth : 998309
                 * mimeType : video/mp4
                 * mime_type : video/mp4
                 * codecs : avc1.640032
                 * width : 1432
                 * height : 1080
                 * frameRate : 29.412
                 * frame_rate : 29.412
                 * sar : 1:1
                 * startWithSap : 1
                 * start_with_sap : 1
                 * SegmentBase : {"Initialization":"0-991","indexRange":"992-1443"}
                 * segment_base : {"initialization":"0-991","index_range":"992-1443"}
                 * codecid : 7
                 */

                private int id;
                private String baseUrl;
                private String base_url;
                private int bandwidth;
                private String mimeType;
                private String mime_type;
                private String codecs;
                private int width;
                private int height;
                private String frameRate;
                private String frame_rate;
                private String sar;
                private int startWithSap;
                private int start_with_sap;
                private SegmentBaseBean SegmentBase;
                private SegmentBaseBeanX segment_base;
                private int codecid;
                private List<String> backupUrl;
                private List<String> backup_url;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getBaseUrl() {
                    return baseUrl;
                }

                public void setBaseUrl(String baseUrl) {
                    this.baseUrl = baseUrl;
                }

                public String getBase_url() {
                    return base_url;
                }

                public void setBase_url(String base_url) {
                    this.base_url = base_url;
                }

                public int getBandwidth() {
                    return bandwidth;
                }

                public void setBandwidth(int bandwidth) {
                    this.bandwidth = bandwidth;
                }

                public String getMimeType() {
                    return mimeType;
                }

                public void setMimeType(String mimeType) {
                    this.mimeType = mimeType;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public String getCodecs() {
                    return codecs;
                }

                public void setCodecs(String codecs) {
                    this.codecs = codecs;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getFrameRate() {
                    return frameRate;
                }

                public void setFrameRate(String frameRate) {
                    this.frameRate = frameRate;
                }

                public String getFrame_rate() {
                    return frame_rate;
                }

                public void setFrame_rate(String frame_rate) {
                    this.frame_rate = frame_rate;
                }

                public String getSar() {
                    return sar;
                }

                public void setSar(String sar) {
                    this.sar = sar;
                }

                public int getStartWithSap() {
                    return startWithSap;
                }

                public void setStartWithSap(int startWithSap) {
                    this.startWithSap = startWithSap;
                }

                public int getStart_with_sap() {
                    return start_with_sap;
                }

                public void setStart_with_sap(int start_with_sap) {
                    this.start_with_sap = start_with_sap;
                }

                public SegmentBaseBean getSegmentBase() {
                    return SegmentBase;
                }

                public void setSegmentBase(SegmentBaseBean SegmentBase) {
                    this.SegmentBase = SegmentBase;
                }

                public SegmentBaseBeanX getSegment_base() {
                    return segment_base;
                }

                public void setSegment_base(SegmentBaseBeanX segment_base) {
                    this.segment_base = segment_base;
                }

                public int getCodecid() {
                    return codecid;
                }

                public void setCodecid(int codecid) {
                    this.codecid = codecid;
                }

                public List<String> getBackupUrl() {
                    return backupUrl;
                }

                public void setBackupUrl(List<String> backupUrl) {
                    this.backupUrl = backupUrl;
                }

                public List<String> getBackup_url() {
                    return backup_url;
                }

                public void setBackup_url(List<String> backup_url) {
                    this.backup_url = backup_url;
                }

                public static class SegmentBaseBean implements Serializable {
                    /**
                     * Initialization : 0-991
                     * indexRange : 992-1443
                     */

                    private String Initialization;
                    private String indexRange;

                    public String getInitialization() {
                        return Initialization;
                    }

                    public void setInitialization(String Initialization) {
                        this.Initialization = Initialization;
                    }

                    public String getIndexRange() {
                        return indexRange;
                    }

                    public void setIndexRange(String indexRange) {
                        this.indexRange = indexRange;
                    }
                }

                public static class SegmentBaseBeanX implements Serializable {
                    /**
                     * initialization : 0-991
                     * index_range : 992-1443
                     */

                    private String initialization;
                    private String index_range;

                    public String getInitialization() {
                        return initialization;
                    }

                    public void setInitialization(String initialization) {
                        this.initialization = initialization;
                    }

                    public String getIndex_range() {
                        return index_range;
                    }

                    public void setIndex_range(String index_range) {
                        this.index_range = index_range;
                    }
                }
            }

            public static class AudioBean implements Serializable {
                /**
                 * id : 30280
                 * baseUrl : https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=eea3841d3665bc56bc68b18082a4fbc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800
                 * base_url : https://xy60x214x170x244xy.mcdn.bilivideo.cn:4483/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=mcdn&oi=1935005709&trid=00008e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=eea3841d3665bc56bc68b18082a4fbc5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=12000195&bvc=vod&nettype=0&orderid=0,3&buvid=&build=0&agrr=1&bw=16255&logo=A0000800
                 * backupUrl : ["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"]
                 * backup_url : ["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/35/90/553099035/553099035-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1670324260&gen=playurlv2&os=alibv&oi=1935005709&trid=8e89791a59cc407abaa7ba05acf7c490u&mid=351201307&platform=pc&upsig=75924d45b2c26e542ee8dc13fb933023&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=&build=0&agrr=1&bw=16255&logo=40000000"]
                 * bandwidth : 129646
                 * mimeType : audio/mp4
                 * mime_type : audio/mp4
                 * codecs : mp4a.40.2
                 * width : 0
                 * height : 0
                 * frameRate :
                 * frame_rate :
                 * sar :
                 * startWithSap : 0
                 * start_with_sap : 0
                 * SegmentBase : {"Initialization":"0-907","indexRange":"908-1371"}
                 * segment_base : {"initialization":"0-907","index_range":"908-1371"}
                 * codecid : 0
                 */

                private int selected;
                private int id;
                private String baseUrl;
                private String base_url;
                private int bandwidth;
                private String mimeType;
                private String mime_type;
                private String codecs;
                private int width;
                private int height;
                private String frameRate;
                private String frame_rate;
                private String sar;
                private int startWithSap;
                private int start_with_sap;
                private SegmentBaseBeanXX SegmentBase;
                private SegmentBaseBeanXXX segment_base;
                private int codecid;
                private List<String> backupUrl;
                private List<String> backup_url;

                public int getSelected() {
                    return selected;
                }

                public void setSelected(int selected) {
                    this.selected = selected;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getBaseUrl() {
                    return baseUrl;
                }

                public void setBaseUrl(String baseUrl) {
                    this.baseUrl = baseUrl;
                }

                public String getBase_url() {
                    return base_url;
                }

                public void setBase_url(String base_url) {
                    this.base_url = base_url;
                }

                public int getBandwidth() {
                    return bandwidth;
                }

                public void setBandwidth(int bandwidth) {
                    this.bandwidth = bandwidth;
                }

                public String getMimeType() {
                    return mimeType;
                }

                public void setMimeType(String mimeType) {
                    this.mimeType = mimeType;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public String getCodecs() {
                    return codecs;
                }

                public void setCodecs(String codecs) {
                    this.codecs = codecs;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getFrameRate() {
                    return frameRate;
                }

                public void setFrameRate(String frameRate) {
                    this.frameRate = frameRate;
                }

                public String getFrame_rate() {
                    return frame_rate;
                }

                public void setFrame_rate(String frame_rate) {
                    this.frame_rate = frame_rate;
                }

                public String getSar() {
                    return sar;
                }

                public void setSar(String sar) {
                    this.sar = sar;
                }

                public int getStartWithSap() {
                    return startWithSap;
                }

                public void setStartWithSap(int startWithSap) {
                    this.startWithSap = startWithSap;
                }

                public int getStart_with_sap() {
                    return start_with_sap;
                }

                public void setStart_with_sap(int start_with_sap) {
                    this.start_with_sap = start_with_sap;
                }

                public SegmentBaseBeanXX getSegmentBase() {
                    return SegmentBase;
                }

                public void setSegmentBase(SegmentBaseBeanXX SegmentBase) {
                    this.SegmentBase = SegmentBase;
                }

                public SegmentBaseBeanXXX getSegment_base() {
                    return segment_base;
                }

                public void setSegment_base(SegmentBaseBeanXXX segment_base) {
                    this.segment_base = segment_base;
                }

                public int getCodecid() {
                    return codecid;
                }

                public void setCodecid(int codecid) {
                    this.codecid = codecid;
                }

                public List<String> getBackupUrl() {
                    return backupUrl;
                }

                public void setBackupUrl(List<String> backupUrl) {
                    this.backupUrl = backupUrl;
                }

                public List<String> getBackup_url() {
                    return backup_url;
                }

                public void setBackup_url(List<String> backup_url) {
                    this.backup_url = backup_url;
                }

                public static class SegmentBaseBeanXX implements Serializable {
                    /**
                     * Initialization : 0-907
                     * indexRange : 908-1371
                     */

                    private String Initialization;
                    private String indexRange;

                    public String getInitialization() {
                        return Initialization;
                    }

                    public void setInitialization(String Initialization) {
                        this.Initialization = Initialization;
                    }

                    public String getIndexRange() {
                        return indexRange;
                    }

                    public void setIndexRange(String indexRange) {
                        this.indexRange = indexRange;
                    }
                }

                public static class SegmentBaseBeanXXX implements Serializable {
                    /**
                     * initialization : 0-907
                     * index_range : 908-1371
                     */

                    private String initialization;
                    private String index_range;

                    public String getInitialization() {
                        return initialization;
                    }

                    public void setInitialization(String initialization) {
                        this.initialization = initialization;
                    }

                    public String getIndex_range() {
                        return index_range;
                    }

                    public void setIndex_range(String index_range) {
                        this.index_range = index_range;
                    }
                }
            }
        }

        public static class SupportFormatsBean implements Serializable {
            /**
             * quality : 112
             * format : hdflv2
             * new_description : 1080P 高码率
             * display_desc : 1080P
             * superscript : 高码率
             * codecs : ["avc1.640032","hev1.1.6.L120.90"]
             */

            private int quality;
            private String format;
            private String new_description;
            private String display_desc;
            private String superscript;
            private List<String> codecs;

            public int getQuality() {
                return quality;
            }

            public void setQuality(int quality) {
                this.quality = quality;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getNew_description() {
                return new_description;
            }

            public void setNew_description(String new_description) {
                this.new_description = new_description;
            }

            public String getDisplay_desc() {
                return display_desc;
            }

            public void setDisplay_desc(String display_desc) {
                this.display_desc = display_desc;
            }

            public String getSuperscript() {
                return superscript;
            }

            public void setSuperscript(String superscript) {
                this.superscript = superscript;
            }

            public List<String> getCodecs() {
                return codecs;
            }

            public void setCodecs(List<String> codecs) {
                this.codecs = codecs;
            }
        }
    }
}
