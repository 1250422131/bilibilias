package com.imcys.bilibilias.common.base.model.bangumi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 番剧剧集明细
 *
 * ![获取剧集明细（web端）（ssid/epid方式）](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/bangumi/info.md#%E8%8E%B7%E5%8F%96%E5%89%A7%E9%9B%86%E6%98%8E%E7%BB%86web%E7%AB%AFssidepid%E6%96%B9%E5%BC%8F)
 *```
 * {
 *   "code": 0,
 *   "message": "success",
 *   "result": {
 *     "activity": {
 *       "head_bg_url": "",
 *       "id": 0,
 *       "title": ""
 *     },
 *     "actors": "",
 *     "alias": "",
 *     "areas": [
 *       {
 *         "id": 3,
 *         "name": "美国"
 *       }
 *     ],
 *     "bkg_cover": "",
 *     "cover": "http://i0.hdslb.com/bfs/bangumi/image/d3ffd19faed45c0139b736f73d07d48e0fd335c6.jpg",
 *     "enable_vt": false,
 *     "episodes": [
 *       {
 *         "aid": 676510178,
 *         "badge": "",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": ""
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV18U4y1g7rR",
 *         "cid": 439868239,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1300000,
 *         "enable_vt": false,
 *         "ep_id": 430636,
 *         "from": "bangumi",
 *         "id": 430636,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430636",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第1话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430636",
 *         "short_link": "https://b23.tv/ep430636",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 2,
 *         "subtitle": "已观看365.7万次",
 *         "title": "1",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 934095041,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1zT4y197Mw",
 *         "cid": 439868574,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1296000,
 *         "enable_vt": false,
 *         "ep_id": 430641,
 *         "from": "bangumi",
 *         "id": 430641,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430641",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第2话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430641",
 *         "short_link": "https://b23.tv/ep430641",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "2",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 464102982,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1qL411u7rB",
 *         "cid": 439868836,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1283000,
 *         "enable_vt": false,
 *         "ep_id": 430633,
 *         "from": "bangumi",
 *         "id": 430633,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430633",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第3话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430633",
 *         "short_link": "https://b23.tv/ep430633",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "3",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 251553504,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1Nv411M7u8",
 *         "cid": 439869033,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 867000,
 *         "enable_vt": false,
 *         "ep_id": 430631,
 *         "from": "bangumi",
 *         "id": 430631,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430631",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第4话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430631",
 *         "short_link": "https://b23.tv/ep430631",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "4",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 764111595,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1Ar4y1y7RC",
 *         "cid": 439869168,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1244000,
 *         "enable_vt": false,
 *         "ep_id": 430637,
 *         "from": "bangumi",
 *         "id": 430637,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430637",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第5话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430637",
 *         "short_link": "https://b23.tv/ep430637",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "5",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 209038926,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1kh411t7nD",
 *         "cid": 439869332,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1288000,
 *         "enable_vt": false,
 *         "ep_id": 430630,
 *         "from": "bangumi",
 *         "id": 430630,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430630",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第6话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430630",
 *         "short_link": "https://b23.tv/ep430630",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "6",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 464056443,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1VL411u7kC",
 *         "cid": 439869598,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1260000,
 *         "enable_vt": false,
 *         "ep_id": 430632,
 *         "from": "bangumi",
 *         "id": 430632,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430632",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第7话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430632",
 *         "short_link": "https://b23.tv/ep430632",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "7",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 806567740,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1t34y1Z7WH",
 *         "cid": 439869890,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1200000,
 *         "enable_vt": false,
 *         "ep_id": 430638,
 *         "from": "bangumi",
 *         "id": 430638,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430638",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第8话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430638",
 *         "short_link": "https://b23.tv/ep430638",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "8",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 506502590,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1Hg411K7Ha",
 *         "cid": 439870093,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1276000,
 *         "enable_vt": false,
 *         "ep_id": 430634,
 *         "from": "bangumi",
 *         "id": 430634,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430634",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第9话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430634",
 *         "short_link": "https://b23.tv/ep430634",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "9",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 849076626,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1SL4y1q7TA",
 *         "cid": 439870338,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1211000,
 *         "enable_vt": false,
 *         "ep_id": 430639,
 *         "from": "bangumi",
 *         "id": 430639,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430639",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第10话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430639",
 *         "short_link": "https://b23.tv/ep430639",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "10",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 506555577,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1tg411K7pg",
 *         "cid": 439870636,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1290000,
 *         "enable_vt": false,
 *         "ep_id": 430635,
 *         "from": "bangumi",
 *         "id": 430635,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430635",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第11话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430635",
 *         "short_link": "https://b23.tv/ep430635",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "11",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 209037416,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1kh411t7h3",
 *         "cid": 439870927,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1244000,
 *         "enable_vt": false,
 *         "ep_id": 430629,
 *         "from": "bangumi",
 *         "id": 430629,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430629",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第12话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430629",
 *         "short_link": "https://b23.tv/ep430629",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "12",
 *         "vid": ""
 *       },
 *       {
 *         "aid": 891564552,
 *         "badge": "会员",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员"
 *         },
 *         "badge_type": 0,
 *         "bvid": "BV1PP4y157JB",
 *         "cid": 439871162,
 *         "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *         "dimension": {
 *           "height": 524,
 *           "rotate": 0,
 *           "width": 720
 *         },
 *         "duration": 1279000,
 *         "enable_vt": false,
 *         "ep_id": 430640,
 *         "from": "bangumi",
 *         "id": 430640,
 *         "is_view_hide": false,
 *         "link": "https://www.bilibili.com/bangumi/play/ep430640",
 *         "long_title": "",
 *         "pub_time": 1636941600,
 *         "pv": 0,
 *         "release_date": "",
 *         "rights": {
 *           "allow_demand": 0,
 *           "allow_dm": 1,
 *           "allow_download": 0,
 *           "area_limit": 0
 *         },
 *         "share_copy": "《猫和老鼠传奇 第二季》第13话 ",
 *         "share_url": "https://www.bilibili.com/bangumi/play/ep430640",
 *         "short_link": "https://b23.tv/ep430640",
 *         "skip": {
 *           "ed": {
 *             "end": 0,
 *             "start": 0
 *           },
 *           "op": {
 *             "end": 0,
 *             "start": 0
 *           }
 *         },
 *         "status": 13,
 *         "subtitle": "已观看365.7万次",
 *         "title": "13",
 *         "vid": ""
 *       }
 *     ],
 *     "evaluate": "汤姆和杰瑞继续着爆笑的追逐游戏，在大街上卖艺，在垃圾场大战，到北极去历险，当然还有很多新面孔出现在他们的战争中……\n",
 *     "freya": {
 *       "bubble_desc": "",
 *       "bubble_show_cnt": 0,
 *       "icon_show": 1
 *     },
 *     "icon_font": {
 *       "name": "playdata-square-line@500",
 *       "text": "365.7万"
 *     },
 *     "jp_title": "",
 *     "link": "http://www.bilibili.com/bangumi/media/md28235595/",
 *     "media_id": 28235595,
 *     "mode": 2,
 *     "new_ep": {
 *       "desc": "已完结, 全13话",
 *       "id": 430640,
 *       "is_new": 0,
 *       "title": "13"
 *     },
 *     "payment": {
 *       "discount": 100,
 *       "pay_type": {
 *         "allow_discount": 0,
 *         "allow_pack": 0,
 *         "allow_ticket": 0,
 *         "allow_time_limit": 0,
 *         "allow_vip_discount": 0,
 *         "forbid_bb": 0
 *       },
 *       "price": "0.0",
 *       "promotion": "",
 *       "tip": "大会员专享观看特权哦~",
 *       "view_start_time": 0,
 *       "vip_discount": 100,
 *       "vip_first_promotion": "",
 *       "vip_price": "0",
 *       "vip_promotion": "成为大会员观看"
 *     },
 *     "play_strategy": {
 *       "strategies": [
 *         "common_section-formal_first_ep",
 *         "common_section-common_section",
 *         "common_section-next_season",
 *         "formal-finish-next_season",
 *         "formal-end-other_section",
 *         "formal-end-next_season",
 *         "ord"
 *       ]
 *     },
 *     "positive": {
 *       "id": 62927,
 *       "title": "正片"
 *     },
 *     "publish": {
 *       "is_finish": 1,
 *       "is_started": 1,
 *       "pub_time": "2021-11-15 10:00:00",
 *       "pub_time_show": "2021年11月15日10:00",
 *       "unknow_pub_date": 0,
 *       "weekday": 0
 *     },
 *     "rating": {
 *       "count": 236,
 *       "score": 9.5
 *     },
 *     "record": "",
 *     "rights": {
 *       "allow_bp": 0,
 *       "allow_bp_rank": 0,
 *       "allow_download": 0,
 *       "allow_review": 1,
 *       "area_limit": 328,
 *       "ban_area_show": 1,
 *       "can_watch": 1,
 *       "copyright": "bilibili",
 *       "forbid_pre": 0,
 *       "freya_white": 0,
 *       "is_cover_show": 0,
 *       "is_preview": 0,
 *       "only_vip_download": 0,
 *       "resource": "",
 *       "watch_platform": 0
 *     },
 *     "season_id": 39901,
 *     "season_title": "猫和老鼠传奇 第二季",
 *     "seasons": [
 *       {
 *         "badge": "",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": ""
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/065926cbda8f464a31293758054620cca15e5589.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "",
 *         "horizontal_cover_169": "",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "3.9亿"
 *         },
 *         "media_id": 132112,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/315d88aa2b1e723089b29ddc2febb5b937ef68c9.jpg",
 *           "id": 249523,
 *           "index_show": "全55话"
 *         },
 *         "season_id": 357,
 *         "season_title": "旧版",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 3220719,
 *           "series_follow": 3571685,
 *           "views": 390283508,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/71c5ec79a7137a08558865463a0649e6b8a4bbd3.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/598b2ed0e194f1d4acb750c47c2d08839f994128.png",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/b524d946ba75b572c4b56705ce4e4b730135798b.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "938万"
 *         },
 *         "media_id": 28235531,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/4b4dba05ad1653d8b6d9f93c4d727294a642b113.png",
 *           "id": 429362,
 *           "index_show": "全16话"
 *         },
 *         "season_id": 39837,
 *         "season_title": "1975版",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 2429079,
 *           "series_follow": 3571685,
 *           "views": 9380068,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/d716a784aad6c1d5778f82004036520520bed474.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/26aecaa7c1820134d03e09eb5f9d942a16491b43.png",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/5a9c6d98d5a1024c0ee2aef8d103ab83340f79a6.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "391.7万"
 *         },
 *         "media_id": 28339474,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/1d44d1f44bbf7302d0c915da745dd0b5e6299612.png",
 *           "id": 674011,
 *           "index_show": "全15话"
 *         },
 *         "season_id": 42922,
 *         "season_title": "1980版",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 2815603,
 *           "series_follow": 3571685,
 *           "views": 3917436,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/3768637495669c08cf4596a56e8d3545259fbe79.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/d479f800c78bb03ec0014f765a6a2ab7bf359145.jpg",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/74bea483ae4563329b785f8db69e32f7c1ea0e7a.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "425.3万"
 *         },
 *         "media_id": 28235482,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/b318f89087b7c3e9b8747c0c97ab82fa3f09deba.png",
 *           "id": 427883,
 *           "index_show": "全13话"
 *         },
 *         "season_id": 39788,
 *         "season_title": "Q版第一季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 54649,
 *           "series_follow": 3571685,
 *           "views": 4252743,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/54a53cd655f7169b6064301f799d698aeba66d59.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/9270bb9d98990aad28128b79b719d0241017a7a0.png",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/5e97d7e84df79c8542518271016791b7349827b7.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "179.7万"
 *         },
 *         "media_id": 28235484,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/5fd162a10a128e7f675939da3fd9835594ed8ba3.png",
 *           "id": 427912,
 *           "index_show": "全13话"
 *         },
 *         "season_id": 39790,
 *         "season_title": "Q版第二季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 36246,
 *           "series_follow": 3571685,
 *           "views": 1797488,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/e3e80d5b340d94933c7f36425d3b3524cbf7a295.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/e93b188ca8aa4d94d6b24cd121bfe8339927ad3c.png",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/9587a050e3dc453d34b54ca716034a723806baea.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "327.3万"
 *         },
 *         "media_id": 28235485,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/5964fa525ca1770beadf0b047e21b7511beb2860.png",
 *           "id": 427951,
 *           "index_show": "全39话"
 *         },
 *         "season_id": 39791,
 *         "season_title": "Q版第三季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 31435,
 *           "series_follow": 3571685,
 *           "views": 3273323,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/f7dd7967c83562f596cabb7e14f1c3caee88b86a.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/41dd9a41c15f99d5a3764c21094ed94303b6b8f2.jpg",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/474d5f887a4a58dc0c3596df1c1c8eb7da34bf6d.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "1684万"
 *         },
 *         "media_id": 28235512,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/1c8f97513d7ff4da7ed67db77601619244665d1a.png",
 *           "id": 429346,
 *           "index_show": "全25话"
 *         },
 *         "season_id": 39818,
 *         "season_title": "新版第一季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 139839,
 *           "series_follow": 3571685,
 *           "views": 16840091,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/6dad1a0bd8e336af8dc83a5fff150922ccf28101.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/cc48a7bb1c718fbd873eace1b510b4f9a5e80d17.png",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/ebcc72a2e79059adf814a790cd47c6eb42235f62.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "1146.5万"
 *         },
 *         "media_id": 28235567,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/7a84a94415804fb874c7b0763f6717649564b850.png",
 *           "id": 429548,
 *           "index_show": "全26话"
 *         },
 *         "season_id": 39873,
 *         "season_title": "新版第二季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 123853,
 *           "series_follow": 3571685,
 *           "views": 11464993,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/9b98897f7e99817fe7c075497eac070b4a59873c.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/4c23a0715274d28e3eec5e90bca55833bbb52bd1.jpg",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/129a1a4eb76bbbddd9383d1ec648a0781e34d87b.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "814.5万"
 *         },
 *         "media_id": 28235596,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/76f7e99cca6578e2227a0124b5c33727a36c081b.png",
 *           "id": 430647,
 *           "index_show": "全24话"
 *         },
 *         "season_id": 39902,
 *         "season_title": "新版第三季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 79349,
 *           "series_follow": 3571685,
 *           "views": 8144940,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/889c2c1c53372eb265f0464834e43c7d9b0b2e6e.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/11baec5947746f03f8318ae75a4b19e82e2f0724.png",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/e4d735e109f807408406426264a8802edb7ee2cd.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "3794.7万"
 *         },
 *         "media_id": 28339473,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/39880dd3c0cf0872e962ed235b0fc6e393c1de17.png",
 *           "id": 674037,
 *           "index_show": "全26话"
 *         },
 *         "season_id": 42921,
 *         "season_title": "新版第四季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 289083,
 *           "series_follow": 3571685,
 *           "views": 37947225,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/439d9ec7a4d189b96adf9617d2f9dcee74542213.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/aec6e0c0541ecc2f7db3bdc429c23bca4c08ae21.png",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/870674a14fba8bff3beaf730f876423e27f3c39b.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "999.9万"
 *         },
 *         "media_id": 28235594,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/e1432353787ed90b06f5a57a90cbf675ffb1101d.png",
 *           "id": 430617,
 *           "index_show": "全13话"
 *         },
 *         "season_id": 39900,
 *         "season_title": "传奇第一季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 105423,
 *           "series_follow": 3571685,
 *           "views": 9998981,
 *           "vt": 0
 *         }
 *       },
 *       {
 *         "badge": "会员专享",
 *         "badge_info": {
 *           "bg_color": "#FB7299",
 *           "bg_color_night": "#BB5B76",
 *           "text": "会员专享"
 *         },
 *         "badge_type": 0,
 *         "cover": "http://i0.hdslb.com/bfs/bangumi/image/d3ffd19faed45c0139b736f73d07d48e0fd335c6.jpg",
 *         "enable_vt": false,
 *         "horizontal_cover_1610": "http://i0.hdslb.com/bfs/bangumi/image/2ca248bb8f1b3975cf8e52257f43e5fede29c377.png",
 *         "horizontal_cover_169": "http://i0.hdslb.com/bfs/bangumi/image/21c7dd2c292d4f2a4d25fea5e36686568e37e6a1.png",
 *         "icon_font": {
 *           "name": "playdata-square-line@500",
 *           "text": "365.7万"
 *         },
 *         "media_id": 28235595,
 *         "new_ep": {
 *           "cover": "http://i0.hdslb.com/bfs/archive/01dfd43073c851087a4d98d37a82ffceb122a74d.png",
 *           "id": 430640,
 *           "index_show": "全13话"
 *         },
 *         "season_id": 39901,
 *         "season_title": "传奇第二季",
 *         "season_type": 1,
 *         "stat": {
 *           "favorites": 43889,
 *           "series_follow": 3571685,
 *           "views": 3656815,
 *           "vt": 0
 *         }
 *       }
 *     ],
 *     "series": {
 *       "display_type": 0,
 *       "series_id": 227,
 *       "series_title": "猫和老鼠"
 *     },
 *     "share_copy": "《猫和老鼠传奇 第二季》猫和老鼠传奇人生",
 *     "share_sub_title": "猫和老鼠传奇人生",
 *     "share_url": "https://www.bilibili.com/bangumi/play/ss39901",
 *     "show": {
 *       "wide_screen": 0
 *     },
 *     "show_season_type": 1,
 *     "square_cover": "http://i0.hdslb.com/bfs/bangumi/image/4877f1134640d4ebfa5b93114dcd10d31960a493.jpg",
 *     "staff": "导演：T.J. House\n编剧：Charles Schneider",
 *     "stat": {
 *       "coins": 3398,
 *       "danmakus": 4664,
 *       "favorite": 2687,
 *       "favorites": 43889,
 *       "follow_text": "357.2万系列追番",
 *       "likes": 18086,
 *       "reply": 518,
 *       "share": 925,
 *       "views": 3656815,
 *       "vt": 0
 *     },
 *     "status": 13,
 *     "styles": [
 *       "搞笑",
 *       "少儿"
 *     ],
 *     "subtitle": "已观看365.7万次",
 *     "title": "猫和老鼠传奇 第二季",
 *     "total": 13,
 *     "type": 1,
 *     "up_info": {
 *       "avatar": "https://i1.hdslb.com/bfs/face/040528a1ec634b9907ba3a9ba957819bb07def06.jpg",
 *       "avatar_subscript_url": "",
 *       "follower": 6336500,
 *       "is_follow": 0,
 *       "mid": 928123,
 *       "nickname_color": "#FB7299",
 *       "pendant": {
 *         "image": "",
 *         "name": "",
 *         "pid": 0
 *       },
 *       "theme_type": 0,
 *       "uname": "哔哩哔哩番剧",
 *       "verify_type": 3,
 *       "vip_label": {
 *         "bg_color": "#FB7299",
 *         "bg_style": 1,
 *         "border_color": "",
 *         "text": "十年大会员",
 *         "text_color": "#FFFFFF"
 *       },
 *       "vip_status": 1,
 *       "vip_type": 2
 *     },
 *     "user_status": {
 *       "area_limit": 0,
 *       "ban_area_show": 0,
 *       "follow": 0,
 *       "follow_status": 0,
 *       "login": 0,
 *       "pay": 0,
 *       "pay_pack_paid": 0,
 *       "sponsor": 0
 *     }
 *   }
 * }
 * ```
 */
@Serializable
data class Bangumi(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("result")
    val result: Result = Result()
) {
    @Serializable
    data class Result(
        @SerialName("activity")
        val activity: Activity = Activity(),
        @SerialName("actors")
        val actors: String = "",
        @SerialName("alias")
        val alias: String = "",
        @SerialName("areas")
        val areas: List<Area> = listOf(),
        @SerialName("bkg_cover")
        val bkgCover: String = "",
        @SerialName("cover")
        val cover: String = "",
        @SerialName("enable_vt")
        val enableVt: Boolean = false,
        @SerialName("episodes")
        val episodes: List<Episode> = listOf(),
        @SerialName("evaluate")
        val evaluate: String = "",
        @SerialName("freya")
        val freya: Freya = Freya(),
        @SerialName("icon_font")
        val iconFont: IconFont = IconFont(),
        @SerialName("jp_title")
        val jpTitle: String = "",
        @SerialName("link")
        val link: String = "",
        @SerialName("media_id")
        val mediaId: Int = 0,
        @SerialName("mode")
        val mode: Int = 0,
        @SerialName("new_ep")
        val newEp: NewEp = NewEp(),
        @SerialName("payment")
        val payment: Payment = Payment(),
        @SerialName("play_strategy")
        val playStrategy: PlayStrategy = PlayStrategy(),
        @SerialName("positive")
        val positive: Positive = Positive(),
        @SerialName("publish")
        val publish: Publish = Publish(),
        @SerialName("rating")
        val rating: Rating = Rating(),
        @SerialName("record")
        val record: String = "",
        @SerialName("rights")
        val rights: Rights = Rights(),
        @SerialName("season_id")
        val seasonId: Int = 0,
        @SerialName("season_title")
        val seasonTitle: String = "",
        @SerialName("seasons")
        val seasons: List<Season> = listOf(),
        @SerialName("series")
        val series: Series = Series(),
        @SerialName("share_copy")
        val shareCopy: String = "",
        @SerialName("share_sub_title")
        val shareSubTitle: String = "",
        @SerialName("share_url")
        val shareUrl: String = "",
        @SerialName("show")
        val show: Show = Show(),
        @SerialName("show_season_type")
        val showSeasonType: Int = 0,
        @SerialName("square_cover")
        val squareCover: String = "",
        @SerialName("staff")
        val staff: String = "",
        @SerialName("stat")
        val stat: Stat = Stat(),
        @SerialName("status")
        val status: Int = 0,
        @SerialName("styles")
        val styles: List<String> = listOf(),
        @SerialName("subtitle")
        val subtitle: String = "",
        @SerialName("title")
        val title: String = "",
        @SerialName("total")
        val total: Int = 0,
        @SerialName("type")
        val type: Int = 0,
        @SerialName("up_info")
        val upInfo: UpInfo = UpInfo(),
        @SerialName("user_status")
        val userStatus: UserStatus = UserStatus()
    ) {
        @Serializable
        data class Activity(
            @SerialName("head_bg_url")
            val headBgUrl: String = "",
            @SerialName("id")
            val id: Int = 0,
            @SerialName("title")
            val title: String = ""
        )

        @Serializable
        data class Area(
            @SerialName("id")
            val id: Int = 0,
            @SerialName("name")
            val name: String = ""
        )

        @Serializable
        data class Episode(
            @SerialName("aid")
            val aid: Long = 0,
            @SerialName("badge")
            val badge: String = "",
            @SerialName("badge_info")
            val badgeInfo: BadgeInfo = BadgeInfo(),
            @SerialName("badge_type")
            val badgeType: Int = 0,
            @SerialName("bvid")
            val bvid: String = "",
            @SerialName("cid")
            val cid: Long = 0,
            @SerialName("cover")
            val cover: String = "",
            @SerialName("dimension")
            val dimension: Dimension = Dimension(),
            @SerialName("duration")
            val duration: Int = 0,
            @SerialName("enable_vt")
            val enableVt: Boolean = false,
            @SerialName("ep_id")
            val epId: Int = 0,
            @SerialName("from")
            val from: String = "",
            @SerialName("id")
            val id: Long = 0,
            @SerialName("is_view_hide")
            val isViewHide: Boolean = false,
            @SerialName("link")
            val link: String = "",
            @SerialName("long_title")
            val longTitle: String = "",
            @SerialName("pub_time")
            val pubTime: Int = 0,
            @SerialName("pv")
            val pv: Int = 0,
            @SerialName("release_date")
            val releaseDate: String = "",
            @SerialName("rights")
            val rights: Rights = Rights(),
            @SerialName("share_copy")
            val shareCopy: String = "",
            @SerialName("share_url")
            val shareUrl: String = "",
            @SerialName("short_link")
            val shortLink: String = "",
            @SerialName("skip")
            val skip: Skip = Skip(),
            @SerialName("status")
            val status: Int = 0,
            @SerialName("subtitle")
            val subtitle: String = "",
            @SerialName("title")
            val title: String = "",
            @SerialName("vid")
            val vid: String = ""
        ) {
            @Serializable
            data class BadgeInfo(
                @SerialName("bg_color")
                val bgColor: String = "",
                @SerialName("bg_color_night")
                val bgColorNight: String = "",
                @SerialName("text")
                val text: String = ""
            )

            @Serializable
            data class Dimension(
                @SerialName("height")
                val height: Int = 0,
                @SerialName("rotate")
                val rotate: Int = 0,
                @SerialName("width")
                val width: Int = 0
            )

            @Serializable
            data class Rights(
                @SerialName("allow_demand")
                val allowDemand: Int = 0,
                @SerialName("allow_dm")
                val allowDm: Int = 0,
                @SerialName("allow_download")
                val allowDownload: Int = 0,
                @SerialName("area_limit")
                val areaLimit: Int = 0
            )

            @Serializable
            data class Skip(
                @SerialName("ed")
                val ed: Ed = Ed(),
                @SerialName("op")
                val op: Op = Op()
            ) {
                @Serializable
                data class Ed(
                    @SerialName("end")
                    val end: Int = 0,
                    @SerialName("start")
                    val start: Int = 0
                )

                @Serializable
                data class Op(
                    @SerialName("end")
                    val end: Int = 0,
                    @SerialName("start")
                    val start: Int = 0
                )
            }
        }

        @Serializable
        data class Freya(
            @SerialName("bubble_desc")
            val bubbleDesc: String = "",
            @SerialName("bubble_show_cnt")
            val bubbleShowCnt: Int = 0,
            @SerialName("icon_show")
            val iconShow: Int = 0
        )

        @Serializable
        data class IconFont(
            @SerialName("name")
            val name: String = "",
            @SerialName("text")
            val text: String = ""
        )

        @Serializable
        data class NewEp(
            @SerialName("desc")
            val desc: String = "",
            @SerialName("id")
            val id: Int = 0,
            @SerialName("is_new")
            val isNew: Int = 0,
            @SerialName("title")
            val title: String = ""
        )

        @Serializable
        data class Payment(
            @SerialName("discount")
            val discount: Int = 0,
            @SerialName("pay_type")
            val payType: PayType = PayType(),
            @SerialName("price")
            val price: String = "",
            @SerialName("promotion")
            val promotion: String = "",
            @SerialName("tip")
            val tip: String = "",
            @SerialName("view_start_time")
            val viewStartTime: Int = 0,
            @SerialName("vip_discount")
            val vipDiscount: Int = 0,
            @SerialName("vip_first_promotion")
            val vipFirstPromotion: String = "",
            @SerialName("vip_price")
            val vipPrice: String = "",
            @SerialName("vip_promotion")
            val vipPromotion: String = ""
        ) {
            @Serializable
            data class PayType(
                @SerialName("allow_discount")
                val allowDiscount: Int = 0,
                @SerialName("allow_pack")
                val allowPack: Int = 0,
                @SerialName("allow_ticket")
                val allowTicket: Int = 0,
                @SerialName("allow_time_limit")
                val allowTimeLimit: Int = 0,
                @SerialName("allow_vip_discount")
                val allowVipDiscount: Int = 0,
                @SerialName("forbid_bb")
                val forbidBb: Int = 0
            )
        }

        @Serializable
        data class PlayStrategy(
            @SerialName("strategies")
            val strategies: List<String> = listOf()
        )

        @Serializable
        data class Positive(
            @SerialName("id")
            val id: Int = 0,
            @SerialName("title")
            val title: String = ""
        )

        @Serializable
        data class Publish(
            @SerialName("is_finish")
            val isFinish: Int = 0,
            @SerialName("is_started")
            val isStarted: Int = 0,
            @SerialName("pub_time")
            val pubTime: String = "",
            @SerialName("pub_time_show")
            val pubTimeShow: String = "",
            @SerialName("unknow_pub_date")
            val unknowPubDate: Int = 0,
            @SerialName("weekday")
            val weekday: Int = 0
        )

        @Serializable
        data class Rating(
            @SerialName("count")
            val count: Int = 0,
            @SerialName("score")
            val score: Double = 0.0
        )

        @Serializable
        data class Rights(
            @SerialName("allow_bp")
            val allowBp: Int = 0,
            @SerialName("allow_bp_rank")
            val allowBpRank: Int = 0,
            @SerialName("allow_download")
            val allowDownload: Int = 0,
            @SerialName("allow_review")
            val allowReview: Int = 0,
            @SerialName("area_limit")
            val areaLimit: Int = 0,
            @SerialName("ban_area_show")
            val banAreaShow: Int = 0,
            @SerialName("can_watch")
            val canWatch: Int = 0,
            @SerialName("copyright")
            val copyright: String = "",
            @SerialName("forbid_pre")
            val forbidPre: Int = 0,
            @SerialName("freya_white")
            val freyaWhite: Int = 0,
            @SerialName("is_cover_show")
            val isCoverShow: Int = 0,
            @SerialName("is_preview")
            val isPreview: Int = 0,
            @SerialName("only_vip_download")
            val onlyVipDownload: Int = 0,
            @SerialName("resource")
            val resource: String = "",
            @SerialName("watch_platform")
            val watchPlatform: Int = 0
        )

        @Serializable
        data class Season(
            @SerialName("badge")
            val badge: String = "",
            @SerialName("badge_info")
            val badgeInfo: BadgeInfo = BadgeInfo(),
            @SerialName("badge_type")
            val badgeType: Int = 0,
            @SerialName("cover")
            val cover: String = "",
            @SerialName("enable_vt")
            val enableVt: Boolean = false,
            @SerialName("horizontal_cover_1610")
            val horizontalCover1610: String = "",
            @SerialName("horizontal_cover_169")
            val horizontalCover169: String = "",
            @SerialName("icon_font")
            val iconFont: IconFont = IconFont(),
            @SerialName("media_id")
            val mediaId: Int = 0,
            @SerialName("new_ep")
            val newEp: NewEp = NewEp(),
            @SerialName("season_id")
            val seasonId: Int = 0,
            @SerialName("season_title")
            val seasonTitle: String = "",
            @SerialName("season_type")
            val seasonType: Int = 0,
            @SerialName("stat")
            val stat: Stat = Stat()
        ) {
            @Serializable
            data class BadgeInfo(
                @SerialName("bg_color")
                val bgColor: String = "",
                @SerialName("bg_color_night")
                val bgColorNight: String = "",
                @SerialName("text")
                val text: String = ""
            )

            @Serializable
            data class IconFont(
                @SerialName("name")
                val name: String = "",
                @SerialName("text")
                val text: String = ""
            )

            @Serializable
            data class NewEp(
                @SerialName("cover")
                val cover: String = "",
                @SerialName("id")
                val id: Int = 0,
                @SerialName("index_show")
                val indexShow: String = ""
            )

            @Serializable
            data class Stat(
                @SerialName("favorites")
                val favorites: Int = 0,
                @SerialName("series_follow")
                val seriesFollow: Int = 0,
                @SerialName("views")
                val views: Int = 0,
                @SerialName("vt")
                val vt: Int = 0
            )
        }

        @Serializable
        data class Series(
            @SerialName("display_type")
            val displayType: Int = 0,
            @SerialName("series_id")
            val seriesId: Int = 0,
            @SerialName("series_title")
            val seriesTitle: String = ""
        )

        @Serializable
        data class Show(
            @SerialName("wide_screen")
            val wideScreen: Int = 0
        )

        @Serializable
        data class Stat(
            @SerialName("coins")
            val coins: Int = 0,
            @SerialName("danmakus")
            val danmakus: Int = 0,
            @SerialName("favorite")
            val favorite: Int = 0,
            @SerialName("favorites")
            val favorites: Int = 0,
            @SerialName("follow_text")
            val followText: String = "",
            @SerialName("likes")
            val likes: Int = 0,
            @SerialName("reply")
            val reply: Int = 0,
            @SerialName("share")
            val share: Int = 0,
            @SerialName("views")
            val views: Int = 0,
            @SerialName("vt")
            val vt: Int = 0
        )

        @Serializable
        data class UpInfo(
            @SerialName("avatar")
            val avatar: String = "",
            @SerialName("avatar_subscript_url")
            val avatarSubscriptUrl: String = "",
            @SerialName("follower")
            val follower: Int = 0,
            @SerialName("is_follow")
            val isFollow: Int = 0,
            @SerialName("mid")
            val mid: Int = 0,
            @SerialName("nickname_color")
            val nicknameColor: String = "",
            @SerialName("pendant")
            val pendant: Pendant = Pendant(),
            @SerialName("theme_type")
            val themeType: Int = 0,
            @SerialName("uname")
            val uname: String = "",
            @SerialName("verify_type")
            val verifyType: Int = 0,
            @SerialName("vip_label")
            val vipLabel: VipLabel = VipLabel(),
            @SerialName("vip_status")
            val vipStatus: Int = 0,
            @SerialName("vip_type")
            val vipType: Int = 0
        ) {
            @Serializable
            data class Pendant(
                @SerialName("image")
                val image: String = "",
                @SerialName("name")
                val name: String = "",
                @SerialName("pid")
                val pid: Int = 0
            )

            @Serializable
            data class VipLabel(
                @SerialName("bg_color")
                val bgColor: String = "",
                @SerialName("bg_style")
                val bgStyle: Int = 0,
                @SerialName("border_color")
                val borderColor: String = "",
                @SerialName("text")
                val text: String = "",
                @SerialName("text_color")
                val textColor: String = ""
            )
        }

        @Serializable
        data class UserStatus(
            @SerialName("area_limit")
            val areaLimit: Int = 0,
            @SerialName("ban_area_show")
            val banAreaShow: Int = 0,
            @SerialName("follow")
            val follow: Int = 0,
            @SerialName("follow_status")
            val followStatus: Int = 0,
            @SerialName("login")
            val login: Int = 0,
            @SerialName("pay")
            val pay: Int = 0,
            @SerialName("pay_pack_paid")
            val payPackPaid: Int = 0,
            @SerialName("sponsor")
            val sponsor: Int = 0
        )
    }
}
