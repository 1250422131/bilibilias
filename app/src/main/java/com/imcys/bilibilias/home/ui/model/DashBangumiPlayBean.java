package com.imcys.bilibilias.home.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author:imcys
 * @create: 2022-12-17 20:05
 * @Description: 番剧dash下载类
 */
public class DashBangumiPlayBean implements Serializable {

    /**
     * code : 0
     * message : success
     * result : {"accept_format":"flv720,flv480,mp4","code":0,"seek_param":"start","is_preview":0,"fnval":80,"video_project":true,"fnver":0,"type":"DASH","bp":0,"result":"suee","seek_type":"offset","from":"local","video_codecid":7,"record_info":{"record_icon":"","record":""},"is_drm":false,"no_rexcode":0,"format":"flv480","support_formats":[{"display_desc":"720P","superscript":"","need_login":true,"codecs":["avc1.64001F"],"format":"flv720","description":"高清 720P","quality":64,"new_description":"720P 高清"},{"display_desc":"480P","superscript":"","codecs":["avc1.64001E"],"format":"flv480","description":"清晰 480P","quality":32,"new_description":"480P 清晰"},{"display_desc":"360P","superscript":"","codecs":["avc1.64001E"],"format":"mp4","description":"流畅 360P","quality":16,"new_description":"360P 流畅"}],"message":"","accept_quality":[64,32,16],"quality":32,"timelength":180202,"has_paid":false,"dash":{"duration":181,"minBufferTime":1.5,"min_buffer_time":1.5,"video":[{"start_with_sap":1,"bandwidth":661565,"sar":"640:639","backupUrl":["https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=82739&logo=40000000","https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=913caf9583a3e6d8ecf26a82cf4cd1c0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=82739&logo=40000000"],"codecs":"avc1.64001E","base_url":"https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=82739&logo=80000000","backup_url":["https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=82739&logo=40000000","https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=913caf9583a3e6d8ecf26a82cf4cd1c0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=82739&logo=40000000"],"segment_base":{"initialization":"0-1010","index_range":"1011-1474"},"mimeType":"video/mp4","frame_rate":"23.810","SegmentBase":{"Initialization":"0-1010","indexRange":"1011-1474"},"frameRate":"23.810","codecid":7,"baseUrl":"https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=82739&logo=80000000","size":14893160,"mime_type":"video/mp4","width":852,"startWithSAP":1,"id":32,"height":480,"md5":""},{"start_with_sap":1,"bandwidth":393062,"sar":"1:1","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=49159&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=49159&logo=40000000"],"codecs":"avc1.64001E","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=b32da86a3b9150d9f50894d55229478c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=49159&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=49159&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=49159&logo=40000000"],"segment_base":{"initialization":"0-1013","index_range":"1014-1477"},"mimeType":"video/mp4","frame_rate":"23.810","SegmentBase":{"Initialization":"0-1013","indexRange":"1014-1477"},"frameRate":"23.810","codecid":7,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=b32da86a3b9150d9f50894d55229478c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=49159&logo=80000000","size":8848621,"mime_type":"video/mp4","width":640,"startWithSAP":1,"id":16,"height":360,"md5":""}],"audio":[{"start_with_sap":0,"bandwidth":97723,"sar":"","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=502a47ab37d7a1b7ce370c768d562a2d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"segment_base":{"initialization":"0-907","index_range":"908-1383"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-907","indexRange":"908-1383"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=502a47ab37d7a1b7ce370c768d562a2d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","size":2201260,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30280,"height":0,"md5":""},{"start_with_sap":0,"bandwidth":67297,"sar":"","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=8421&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=8421&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=679ddf874a00e5211311431dd3dcba96&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=8421&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=8421&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=8421&logo=40000000"],"segment_base":{"initialization":"0-940","index_range":"941-1416"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-940","indexRange":"941-1416"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=679ddf874a00e5211311431dd3dcba96&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=8421&logo=80000000","size":1515906,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30216,"height":0,"md5":""},{"start_with_sap":0,"bandwidth":97723,"sar":"","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=cb4f435bc072b769110c3acabbc572e6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=2d46c7384b27a4d971edfc5110e69b66&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=16c2e6097c93cd42adb2fa80de99312f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=cb4f435bc072b769110c3acabbc572e6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=2d46c7384b27a4d971edfc5110e69b66&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"segment_base":{"initialization":"0-907","index_range":"908-1383"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-907","indexRange":"908-1383"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=16c2e6097c93cd42adb2fa80de99312f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","size":2201260,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30232,"height":0,"md5":""}],"dolby":{"audio":[],"type":0}},"clip_info_list":[{"materialNo":0,"start":15,"end":21,"toastText":"即将跳过片头","clipType":"CLIP_TYPE_OP"},{"materialNo":0,"start":151,"end":180,"toastText":"即将跳过片尾","clipType":"CLIP_TYPE_ED"}],"accept_description":["高清 720P","清晰 480P","流畅 360P"],"status":2}
     */

    private int code;
    private String message;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * accept_format : flv720,flv480,mp4
         * code : 0
         * seek_param : start
         * is_preview : 0
         * fnval : 80
         * video_project : true
         * fnver : 0
         * type : DASH
         * bp : 0
         * result : suee
         * seek_type : offset
         * from : local
         * video_codecid : 7
         * record_info : {"record_icon":"","record":""}
         * is_drm : false
         * no_rexcode : 0
         * format : flv480
         * support_formats : [{"display_desc":"720P","superscript":"","need_login":true,"codecs":["avc1.64001F"],"format":"flv720","description":"高清 720P","quality":64,"new_description":"720P 高清"},{"display_desc":"480P","superscript":"","codecs":["avc1.64001E"],"format":"flv480","description":"清晰 480P","quality":32,"new_description":"480P 清晰"},{"display_desc":"360P","superscript":"","codecs":["avc1.64001E"],"format":"mp4","description":"流畅 360P","quality":16,"new_description":"360P 流畅"}]
         * message :
         * accept_quality : [64,32,16]
         * quality : 32
         * timelength : 180202
         * has_paid : false
         * dash : {"duration":181,"minBufferTime":1.5,"min_buffer_time":1.5,"video":[{"start_with_sap":1,"bandwidth":661565,"sar":"640:639","backupUrl":["https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=82739&logo=40000000","https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=913caf9583a3e6d8ecf26a82cf4cd1c0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=82739&logo=40000000"],"codecs":"avc1.64001E","base_url":"https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=82739&logo=80000000","backup_url":["https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=82739&logo=40000000","https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=913caf9583a3e6d8ecf26a82cf4cd1c0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=82739&logo=40000000"],"segment_base":{"initialization":"0-1010","index_range":"1011-1474"},"mimeType":"video/mp4","frame_rate":"23.810","SegmentBase":{"Initialization":"0-1010","indexRange":"1011-1474"},"frameRate":"23.810","codecid":7,"baseUrl":"https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=82739&logo=80000000","size":14893160,"mime_type":"video/mp4","width":852,"startWithSAP":1,"id":32,"height":480,"md5":""},{"start_with_sap":1,"bandwidth":393062,"sar":"1:1","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=49159&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=49159&logo=40000000"],"codecs":"avc1.64001E","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=b32da86a3b9150d9f50894d55229478c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=49159&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=49159&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=49159&logo=40000000"],"segment_base":{"initialization":"0-1013","index_range":"1014-1477"},"mimeType":"video/mp4","frame_rate":"23.810","SegmentBase":{"Initialization":"0-1013","indexRange":"1014-1477"},"frameRate":"23.810","codecid":7,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=b32da86a3b9150d9f50894d55229478c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=49159&logo=80000000","size":8848621,"mime_type":"video/mp4","width":640,"startWithSAP":1,"id":16,"height":360,"md5":""}],"audio":[{"start_with_sap":0,"bandwidth":97723,"sar":"","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=502a47ab37d7a1b7ce370c768d562a2d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"segment_base":{"initialization":"0-907","index_range":"908-1383"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-907","indexRange":"908-1383"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=502a47ab37d7a1b7ce370c768d562a2d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","size":2201260,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30280,"height":0,"md5":""},{"start_with_sap":0,"bandwidth":67297,"sar":"","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=8421&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=8421&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=679ddf874a00e5211311431dd3dcba96&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=8421&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=8421&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=8421&logo=40000000"],"segment_base":{"initialization":"0-940","index_range":"941-1416"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-940","indexRange":"941-1416"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=679ddf874a00e5211311431dd3dcba96&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=8421&logo=80000000","size":1515906,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30216,"height":0,"md5":""},{"start_with_sap":0,"bandwidth":97723,"sar":"","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=cb4f435bc072b769110c3acabbc572e6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=2d46c7384b27a4d971edfc5110e69b66&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=16c2e6097c93cd42adb2fa80de99312f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=cb4f435bc072b769110c3acabbc572e6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=2d46c7384b27a4d971edfc5110e69b66&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"segment_base":{"initialization":"0-907","index_range":"908-1383"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-907","indexRange":"908-1383"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=16c2e6097c93cd42adb2fa80de99312f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","size":2201260,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30232,"height":0,"md5":""}],"dolby":{"audio":[],"type":0}}
         * clip_info_list : [{"materialNo":0,"start":15,"end":21,"toastText":"即将跳过片头","clipType":"CLIP_TYPE_OP"},{"materialNo":0,"start":151,"end":180,"toastText":"即将跳过片尾","clipType":"CLIP_TYPE_ED"}]
         * accept_description : ["高清 720P","清晰 480P","流畅 360P"]
         * status : 2
         */

        private String accept_format;
        private int code;
        private String seek_param;
        private int is_preview;
        private int fnval;
        private boolean video_project;
        private int fnver;
        private String type;
        private int bp;
        private String result;
        private String seek_type;
        private String from;
        private int video_codecid;
        private RecordInfoBean record_info;
        private boolean is_drm;
        private int no_rexcode;
        private String format;
        private String message;
        private int quality;
        private int timelength;
        private boolean has_paid;
        private DashBean dash;
        private int status;
        private List<SupportFormatsBean> support_formats;
        private List<Integer> accept_quality;
        private List<ClipInfoListBean> clip_info_list;
        private List<String> accept_description;

        public String getAccept_format() {
            return accept_format;
        }

        public void setAccept_format(String accept_format) {
            this.accept_format = accept_format;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getSeek_param() {
            return seek_param;
        }

        public void setSeek_param(String seek_param) {
            this.seek_param = seek_param;
        }

        public int getIs_preview() {
            return is_preview;
        }

        public void setIs_preview(int is_preview) {
            this.is_preview = is_preview;
        }

        public int getFnval() {
            return fnval;
        }

        public void setFnval(int fnval) {
            this.fnval = fnval;
        }

        public boolean isVideo_project() {
            return video_project;
        }

        public void setVideo_project(boolean video_project) {
            this.video_project = video_project;
        }

        public int getFnver() {
            return fnver;
        }

        public void setFnver(int fnver) {
            this.fnver = fnver;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getBp() {
            return bp;
        }

        public void setBp(int bp) {
            this.bp = bp;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getSeek_type() {
            return seek_type;
        }

        public void setSeek_type(String seek_type) {
            this.seek_type = seek_type;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public int getVideo_codecid() {
            return video_codecid;
        }

        public void setVideo_codecid(int video_codecid) {
            this.video_codecid = video_codecid;
        }

        public RecordInfoBean getRecord_info() {
            return record_info;
        }

        public void setRecord_info(RecordInfoBean record_info) {
            this.record_info = record_info;
        }

        public boolean isIs_drm() {
            return is_drm;
        }

        public void setIs_drm(boolean is_drm) {
            this.is_drm = is_drm;
        }

        public int getNo_rexcode() {
            return no_rexcode;
        }

        public void setNo_rexcode(int no_rexcode) {
            this.no_rexcode = no_rexcode;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
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

        public int getTimelength() {
            return timelength;
        }

        public void setTimelength(int timelength) {
            this.timelength = timelength;
        }

        public boolean isHas_paid() {
            return has_paid;
        }

        public void setHas_paid(boolean has_paid) {
            this.has_paid = has_paid;
        }

        public DashBean getDash() {
            return dash;
        }

        public void setDash(DashBean dash) {
            this.dash = dash;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<SupportFormatsBean> getSupport_formats() {
            return support_formats;
        }

        public void setSupport_formats(List<SupportFormatsBean> support_formats) {
            this.support_formats = support_formats;
        }

        public List<Integer> getAccept_quality() {
            return accept_quality;
        }

        public void setAccept_quality(List<Integer> accept_quality) {
            this.accept_quality = accept_quality;
        }

        public List<ClipInfoListBean> getClip_info_list() {
            return clip_info_list;
        }

        public void setClip_info_list(List<ClipInfoListBean> clip_info_list) {
            this.clip_info_list = clip_info_list;
        }

        public List<String> getAccept_description() {
            return accept_description;
        }

        public void setAccept_description(List<String> accept_description) {
            this.accept_description = accept_description;
        }

        public static class RecordInfoBean implements Serializable {
            /**
             * record_icon :
             * record :
             */

            private String record_icon;
            private String record;

            public String getRecord_icon() {
                return record_icon;
            }

            public void setRecord_icon(String record_icon) {
                this.record_icon = record_icon;
            }

            public String getRecord() {
                return record;
            }

            public void setRecord(String record) {
                this.record = record;
            }
        }

        public static class DashBean implements Serializable {
            /**
             * duration : 181
             * minBufferTime : 1.5
             * min_buffer_time : 1.5
             * video : [{"start_with_sap":1,"bandwidth":661565,"sar":"640:639","backupUrl":["https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=82739&logo=40000000","https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=913caf9583a3e6d8ecf26a82cf4cd1c0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=82739&logo=40000000"],"codecs":"avc1.64001E","base_url":"https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=82739&logo=80000000","backup_url":["https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=82739&logo=40000000","https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=913caf9583a3e6d8ecf26a82cf4cd1c0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=82739&logo=40000000"],"segment_base":{"initialization":"0-1010","index_range":"1011-1474"},"mimeType":"video/mp4","frame_rate":"23.810","SegmentBase":{"Initialization":"0-1010","indexRange":"1011-1474"},"frameRate":"23.810","codecid":7,"baseUrl":"https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=82739&logo=80000000","size":14893160,"mime_type":"video/mp4","width":852,"startWithSAP":1,"id":32,"height":480,"md5":""},{"start_with_sap":1,"bandwidth":393062,"sar":"1:1","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=49159&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=49159&logo=40000000"],"codecs":"avc1.64001E","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=b32da86a3b9150d9f50894d55229478c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=49159&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=49159&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=5682cfb0606e69073347c285ab89ecc6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=49159&logo=40000000"],"segment_base":{"initialization":"0-1013","index_range":"1014-1477"},"mimeType":"video/mp4","frame_rate":"23.810","SegmentBase":{"Initialization":"0-1013","indexRange":"1014-1477"},"frameRate":"23.810","codecid":7,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30016.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=b32da86a3b9150d9f50894d55229478c&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=49159&logo=80000000","size":8848621,"mime_type":"video/mp4","width":640,"startWithSAP":1,"id":16,"height":360,"md5":""}]
             * audio : [{"start_with_sap":0,"bandwidth":97723,"sar":"","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=502a47ab37d7a1b7ce370c768d562a2d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"segment_base":{"initialization":"0-907","index_range":"908-1383"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-907","indexRange":"908-1383"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=502a47ab37d7a1b7ce370c768d562a2d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","size":2201260,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30280,"height":0,"md5":""},{"start_with_sap":0,"bandwidth":67297,"sar":"","backupUrl":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=8421&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=8421&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=679ddf874a00e5211311431dd3dcba96&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=8421&logo=80000000","backup_url":["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=8421&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=d36a0f90c57c603d0e8cb0d5584c68b5&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=8421&logo=40000000"],"segment_base":{"initialization":"0-940","index_range":"941-1416"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-940","indexRange":"941-1416"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30216.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=679ddf874a00e5211311431dd3dcba96&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=8421&logo=80000000","size":1515906,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30216,"height":0,"md5":""},{"start_with_sap":0,"bandwidth":97723,"sar":"","backupUrl":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=cb4f435bc072b769110c3acabbc572e6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=2d46c7384b27a4d971edfc5110e69b66&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"codecs":"mp4a.40.2","base_url":"https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=16c2e6097c93cd42adb2fa80de99312f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","backup_url":["https://upos-sz-mirrorali.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=cb4f435bc072b769110c3acabbc572e6&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirroralib.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=alibbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=2d46c7384b27a4d971edfc5110e69b66&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"],"segment_base":{"initialization":"0-907","index_range":"908-1383"},"mimeType":"audio/mp4","frame_rate":"","SegmentBase":{"Initialization":"0-907","indexRange":"908-1383"},"frameRate":"","codecid":0,"baseUrl":"https://upos-sz-estgoss.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30232.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=16c2e6097c93cd42adb2fa80de99312f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000","size":2201260,"mime_type":"audio/mp4","width":0,"startWithSAP":0,"id":30232,"height":0,"md5":""}]
             * dolby : {"audio":[],"type":0}
             */

            private int duration;
            private double minBufferTime;
            private double min_buffer_time;
            private DolbyBean dolby;
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
                 * audio : []
                 * type : 0
                 */

                private int type;
                private List<?> audio;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public List<?> getAudio() {
                    return audio;
                }

                public void setAudio(List<?> audio) {
                    this.audio = audio;
                }
            }

            public static class VideoBean implements Serializable {
                /**
                 * start_with_sap : 1
                 * bandwidth : 661565
                 * sar : 640:639
                 * backupUrl : ["https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=82739&logo=40000000","https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=913caf9583a3e6d8ecf26a82cf4cd1c0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=82739&logo=40000000"]
                 * codecs : avc1.64001E
                 * base_url : https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=82739&logo=80000000
                 * backup_url : ["https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=82739&logo=40000000","https://upos-sz-mirrorcosb.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=913caf9583a3e6d8ecf26a82cf4cd1c0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=82739&logo=40000000"]
                 * segment_base : {"initialization":"0-1010","index_range":"1011-1474"}
                 * mimeType : video/mp4
                 * frame_rate : 23.810
                 * SegmentBase : {"Initialization":"0-1010","indexRange":"1011-1474"}
                 * frameRate : 23.810
                 * codecid : 7
                 * baseUrl : https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30032.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=cosbv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=35cbf6e8970a6f6a172b9846a3749892&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=82739&logo=80000000
                 * size : 14893160
                 * mime_type : video/mp4
                 * width : 852
                 * startWithSAP : 1
                 * id : 32
                 * height : 480
                 * md5 :
                 */

                private int start_with_sap;
                private int bandwidth;
                private String sar;
                private String codecs;
                private String base_url;
                private SegmentBaseBean segment_base;
                private String mimeType;
                private String frame_rate;
                private SegmentBaseBeanX SegmentBase;
                private String frameRate;
                private int codecid;
                private String baseUrl;
                private Long size;
                private String mime_type;
                private int width;
                private int startWithSAP;
                private int id;
                private int height;
                private String md5;
                private List<String> backupUrl;
                private List<String> backup_url;

                public int getStart_with_sap() {
                    return start_with_sap;
                }

                public void setStart_with_sap(int start_with_sap) {
                    this.start_with_sap = start_with_sap;
                }

                public int getBandwidth() {
                    return bandwidth;
                }

                public void setBandwidth(int bandwidth) {
                    this.bandwidth = bandwidth;
                }

                public String getSar() {
                    return sar;
                }

                public void setSar(String sar) {
                    this.sar = sar;
                }

                public String getCodecs() {
                    return codecs;
                }

                public void setCodecs(String codecs) {
                    this.codecs = codecs;
                }

                public String getBase_url() {
                    return base_url;
                }

                public void setBase_url(String base_url) {
                    this.base_url = base_url;
                }

                public SegmentBaseBean getSegment_base() {
                    return segment_base;
                }

                public void setSegment_base(SegmentBaseBean segment_base) {
                    this.segment_base = segment_base;
                }

                public String getMimeType() {
                    return mimeType;
                }

                public void setMimeType(String mimeType) {
                    this.mimeType = mimeType;
                }

                public String getFrame_rate() {
                    return frame_rate;
                }

                public void setFrame_rate(String frame_rate) {
                    this.frame_rate = frame_rate;
                }

                public SegmentBaseBeanX getSegmentBase() {
                    return SegmentBase;
                }

                public void setSegmentBase(SegmentBaseBeanX SegmentBase) {
                    this.SegmentBase = SegmentBase;
                }

                public String getFrameRate() {
                    return frameRate;
                }

                public void setFrameRate(String frameRate) {
                    this.frameRate = frameRate;
                }

                public int getCodecid() {
                    return codecid;
                }

                public void setCodecid(int codecid) {
                    this.codecid = codecid;
                }

                public String getBaseUrl() {
                    return baseUrl;
                }

                public void setBaseUrl(String baseUrl) {
                    this.baseUrl = baseUrl;
                }

                public Long getSize() {
                    return size;
                }

                public void setSize(Long size) {
                    this.size = size;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getStartWithSAP() {
                    return startWithSAP;
                }

                public void setStartWithSAP(int startWithSAP) {
                    this.startWithSAP = startWithSAP;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
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
                     * initialization : 0-1010
                     * index_range : 1011-1474
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

                public static class SegmentBaseBeanX implements Serializable {
                    /**
                     * Initialization : 0-1010
                     * indexRange : 1011-1474
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
            }

            public static class AudioBean implements Serializable {
                /**
                 * start_with_sap : 0
                 * bandwidth : 97723
                 * sar :
                 * backupUrl : ["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"]
                 * codecs : mp4a.40.2
                 * base_url : https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=502a47ab37d7a1b7ce370c768d562a2d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000
                 * backup_url : ["https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&agrr=0&bw=12229&logo=40000000","https://upos-sz-mirrorali02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=ali02bv&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=c614ea4132774db8b316785148af31dc&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&agrr=0&bw=12229&logo=40000000"]
                 * segment_base : {"initialization":"0-907","index_range":"908-1383"}
                 * mimeType : audio/mp4
                 * frame_rate :
                 * SegmentBase : {"Initialization":"0-907","indexRange":"908-1383"}
                 * frameRate :
                 * codecid : 0
                 * baseUrl : https://upos-sz-estgoss02.bilivideo.com/upgcxcode/87/44/674487/674487_p1-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1671285744&gen=playurlv2&os=upos&oi=1935005709&trid=0dae74aa72c6455da2a63620d5fa47fap&mid=0&platform=pc&upsig=502a47ab37d7a1b7ce370c768d562a2d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=0,3&agrr=0&bw=12229&logo=80000000
                 * size : 2201260
                 * mime_type : audio/mp4
                 * width : 0
                 * startWithSAP : 0
                 * id : 30280
                 * height : 0
                 * md5 :
                 */

                private int start_with_sap;
                private int bandwidth;
                private String sar;
                private String codecs;
                private String base_url;
                private SegmentBaseBeanXX segment_base;
                private String mimeType;
                private String frame_rate;
                private SegmentBaseBeanXXX SegmentBase;
                private String frameRate;
                private int codecid;
                private String baseUrl;
                private Long size;
                private String mime_type;
                private int width;
                private int startWithSAP;
                private int id;
                private int height;
                private String md5;
                private List<String> backupUrl;
                private List<String> backup_url;

                public int getStart_with_sap() {
                    return start_with_sap;
                }

                public void setStart_with_sap(int start_with_sap) {
                    this.start_with_sap = start_with_sap;
                }

                public int getBandwidth() {
                    return bandwidth;
                }

                public void setBandwidth(int bandwidth) {
                    this.bandwidth = bandwidth;
                }

                public String getSar() {
                    return sar;
                }

                public void setSar(String sar) {
                    this.sar = sar;
                }

                public String getCodecs() {
                    return codecs;
                }

                public void setCodecs(String codecs) {
                    this.codecs = codecs;
                }

                public String getBase_url() {
                    return base_url;
                }

                public void setBase_url(String base_url) {
                    this.base_url = base_url;
                }

                public SegmentBaseBeanXX getSegment_base() {
                    return segment_base;
                }

                public void setSegment_base(SegmentBaseBeanXX segment_base) {
                    this.segment_base = segment_base;
                }

                public String getMimeType() {
                    return mimeType;
                }

                public void setMimeType(String mimeType) {
                    this.mimeType = mimeType;
                }

                public String getFrame_rate() {
                    return frame_rate;
                }

                public void setFrame_rate(String frame_rate) {
                    this.frame_rate = frame_rate;
                }

                public SegmentBaseBeanXXX getSegmentBase() {
                    return SegmentBase;
                }

                public void setSegmentBase(SegmentBaseBeanXXX SegmentBase) {
                    this.SegmentBase = SegmentBase;
                }

                public String getFrameRate() {
                    return frameRate;
                }

                public void setFrameRate(String frameRate) {
                    this.frameRate = frameRate;
                }

                public int getCodecid() {
                    return codecid;
                }

                public void setCodecid(int codecid) {
                    this.codecid = codecid;
                }

                public String getBaseUrl() {
                    return baseUrl;
                }

                public void setBaseUrl(String baseUrl) {
                    this.baseUrl = baseUrl;
                }

                public Long getSize() {
                    return size;
                }

                public void setSize(Long size) {
                    this.size = size;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getStartWithSAP() {
                    return startWithSAP;
                }

                public void setStartWithSAP(int startWithSAP) {
                    this.startWithSAP = startWithSAP;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
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
                     * initialization : 0-907
                     * index_range : 908-1383
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

                public static class SegmentBaseBeanXXX implements Serializable {
                    /**
                     * Initialization : 0-907
                     * indexRange : 908-1383
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
            }
        }

        public static class SupportFormatsBean implements Serializable {
            /**
             * display_desc : 720P
             * superscript :
             * need_login : true
             * codecs : ["avc1.64001F"]
             * format : flv720
             * description : 高清 720P
             * quality : 64
             * new_description : 720P 高清
             */

            private String display_desc;
            private String superscript;
            private boolean need_login;
            private String format;
            private String description;
            private int quality;
            private String new_description;
            private List<String> codecs;

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

            public boolean isNeed_login() {
                return need_login;
            }

            public void setNeed_login(boolean need_login) {
                this.need_login = need_login;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getQuality() {
                return quality;
            }

            public void setQuality(int quality) {
                this.quality = quality;
            }

            public String getNew_description() {
                return new_description;
            }

            public void setNew_description(String new_description) {
                this.new_description = new_description;
            }

            public List<String> getCodecs() {
                return codecs;
            }

            public void setCodecs(List<String> codecs) {
                this.codecs = codecs;
            }
        }

        public static class ClipInfoListBean implements Serializable {
            /**
             * materialNo : 0
             * start : 15
             * end : 21
             * toastText : 即将跳过片头
             * clipType : CLIP_TYPE_OP
             */

            private int materialNo;
            private int start;
            private int end;
            private String toastText;
            private String clipType;

            public int getMaterialNo() {
                return materialNo;
            }

            public void setMaterialNo(int materialNo) {
                this.materialNo = materialNo;
            }

            public int getStart() {
                return start;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public int getEnd() {
                return end;
            }

            public void setEnd(int end) {
                this.end = end;
            }

            public String getToastText() {
                return toastText;
            }

            public void setToastText(String toastText) {
                this.toastText = toastText;
            }

            public String getClipType() {
                return clipType;
            }

            public void setClipType(String clipType) {
                this.clipType = clipType;
            }
        }
    }
}
