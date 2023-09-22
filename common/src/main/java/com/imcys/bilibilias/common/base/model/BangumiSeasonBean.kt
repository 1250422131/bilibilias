package com.imcys.bilibilias.common.base.model

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
class BangumiSeasonBean {
    val code = 0
    val message: String = ""
    val result: ResultBean = ResultBean()

    @Serializable class ResultBean {
        val activity: ActivityBean = ActivityBean()
        val alias: String = ""
        val bkg_cover: String = ""
        val cover: String = ""
        val evaluate: String = ""
        val freya: FreyaBean = FreyaBean()
        val jp_title: String = ""
        val link: String = ""
        val media_id = 0
        val mode = 0
        val new_ep: NewEpBean = NewEpBean()
        val payment: PaymentBean = PaymentBean()
        val positive: PositiveBean = PositiveBean()
        val publish: PublishBean = PublishBean()
        val rating: RatingBean = RatingBean()
        val record: String = ""
        val rights: RightsBean = RightsBean()
        val season_id = 0
        val season_title: String = ""
        val series: SeriesBean = SeriesBean()
        val share_copy: String = ""
        val share_sub_title: String = ""
        val share_url: String = ""
        val show: ShowBean = ShowBean()
        val show_season_type = 0
        val square_cover: String = ""
        val stat: StatBean = StatBean()
        val status = 0
        val subtitle: String = ""
        val title: String = ""
        val total = 0
        val type = 0
        val up_info: UpInfoBean = UpInfoBean()
        val user_status: UserStatusBean = UserStatusBean()
        val areas: List<AreasBean> = emptyList()
        val episodes: List<EpisodesBean> = emptyList()

        @Serializable class ActivityBean {
            /**
             * head_bg_url :
             * id : 1634
             * title : 三体播放页会员购装扮中插
             */
            val head_bg_url: String = ""
            val id = 0
            val title: String = ""
        }

        @Serializable class FreyaBean {
            /**
             * bubble_desc : 你收到1个打招呼
             * bubble_show_cnt : 10000
             * icon_show : 1
             */
            val bubble_desc: String = ""
            val bubble_show_cnt = 0
            val icon_show = 0
        }

        @Serializable class NewEpBean {
            /**
             * desc : 连载中, 每周六11点，第5集起会员专享
             * id : 704475
             * is_new : 1
             * title : 3
             */
            val desc: String = ""
            val id = 0
            val is_new = 0
            val title: String = ""
        }

        @Serializable class PaymentBean {
            /**
             * discount : 100
             * pay_type : {"allow_discount":0,"allow_pack":0,"allow_ticket":0,"allow_time_limit":0,"allow_vip_discount":0,"forbid_bb":0}
             * price : 0.0
             * promotion :
             * tip : 大会员专享观看特权哦~
             * view_start_time : 0
             * vip_discount : 100
             * vip_first_promotion :
             * vip_promotion : 成为大会员抢先看
             */
            val discount = 0
            val pay_type: PayTypeBean = PayTypeBean()
            val price: String = ""
            val promotion: String = ""
            val tip: String = ""
            val view_start_time = 0
            val vip_discount = 0
            val vip_first_promotion: String = ""
            val vip_promotion: String = ""

            @Serializable class PayTypeBean {
                /**
                 * allow_discount : 0
                 * allow_pack : 0
                 * allow_ticket : 0
                 * allow_time_limit : 0
                 * allow_vip_discount : 0
                 * forbid_bb : 0
                 */
                val allow_discount = 0
                val allow_pack = 0
                val allow_ticket = 0
                val allow_time_limit = 0
                val allow_vip_discount = 0
                val forbid_bb = 0
            }
        }

        @Serializable class PositiveBean {
            /**
             * id : 35659
             * title : 正片
             */
            val id = 0
            val title: String = ""
        }

        @Serializable class PublishBean {
            /**
             * is_finish : 0
             * is_started : 1
             * pub_time : 2022-12-10 11:00:00
             * pub_time_show : 12月10日11:00
             * unknow_pub_date : 0
             * weekday : 0
             */
            val is_finish = 0
            val is_started = 0
            val pub_time: String = ""
            val pub_time_show: String = ""
            val unknow_pub_date = 0
            val weekday = 0
        }

        @Serializable class RatingBean {
            /**
             * count : 7575
             * score : 8.2
             */
            val count = 0
            val score = 0.0
        }

        @Serializable class RightsBean {
            /**
             * allow_bp : 0
             * allow_bp_rank : 0
             * allow_download : 0
             * allow_review : 1
             * area_limit : 340
             * ban_area_show : 1
             * can_watch : 1
             * copyright : dujia
             * forbid_pre : 0
             * freya_white : 0
             * is_cover_show : 0
             * is_preview : 1
             * only_vip_download : 0
             * resource :
             * watch_platform : 0
             */
            val allow_bp = 0
            val allow_bp_rank = 0
            val allow_download = 0
            val allow_review = 0
            val area_limit = 0
            val ban_area_show = 0
            val can_watch = 0
            val copyright: String = ""
            val forbid_pre = 0
            val freya_white = 0
            val is_cover_show = 0
            val is_preview = 0
            val only_vip_download = 0
            val resource: String = ""
            val watch_platform = 0
        }

        @Serializable class SeriesBean {
            /**
             * display_type : 0
             * series_id : 4260
             * series_title : 三体
             */
            val display_type = 0
            val series_id = 0
            val series_title: String = ""
        }

        @Serializable class ShowBean {
            /**
             * wide_screen : 0
             */
            val wide_screen = 0
        }

        @Serializable class StatBean {
            /**
             * coins : 1642423
             * danmakus : 330118
             * favorite : 406697
             * favorites : 6512751
             * likes : 2835320
             * reply : 362891
             * share : 1129706
             * views : 161123948
             */
            val coins = 0
            val danmakus = 0
            val favorite = 0
            val favorites = 0
            val likes = 0
            val reply = 0
            val share = 0
            val views = 0
        }

        @Serializable class UpInfoBean {
            /**
             * avatar : https://i2.hdslb.com/bfs/face/38fbf0a10f7fb0e8910be0e662b05bcb0aed830c.jpg
             * avatar_subscript_url :
             * follower : 4583274
             * is_follow : 0
             * mid : 98627270
             * nickname_color : #FB7299
             * pendant : {"image":"https://i2.hdslb.com/bfs/garb/item/4ab1a5a6e07a99e649cde625c06eeb1c15585156.png","name":"罗小黑战记","pid":5108}
             * theme_type : 0
             * uname : 哔哩哔哩国创
             * verify_type : 3
             * vip_label : {"bg_color":"#FB7299","bg_style":1,"border_color":"","text":"十年大会员","text_color":"#FFFFFF"}
             * vip_status : 1
             * vip_type : 2
             */
            val avatar: String = ""
            val avatar_subscript_url: String = ""
            val follower = 0
            val is_follow = 0
            val mid: Long = 0
            val nickname_color: String = ""
            val pendant: PendantBean = PendantBean()
            val theme_type = 0
            val uname: String = ""
            val verify_type = 0
            val vip_label: VipLabelBean = VipLabelBean()
            val vip_status = 0
            val vip_type = 0

            @Serializable class PendantBean {
                /**
                 * image : https://i2.hdslb.com/bfs/garb/item/4ab1a5a6e07a99e649cde625c06eeb1c15585156.png
                 * name : 罗小黑战记
                 * pid : 5108
                 */
                val image: String = ""
                val name: String = ""
                val pid = 0
            }

            @Serializable class VipLabelBean {
                /**
                 * bg_color : #FB7299
                 * bg_style : 1
                 * border_color :
                 * text : 十年大会员
                 * text_color : #FFFFFF
                 */
                val bg_color: String = ""
                val bg_style = 0
                val border_color: String = ""
                val text: String = ""
                val text_color: String = ""
            }
        }

        @Serializable class UserStatusBean {
            /**
             * area_limit : 0
             * ban_area_show : 0
             * follow : 0
             * follow_status : 0
             * login : 0
             * pay : 0
             * pay_pack_paid : 0
             * sponsor : 0
             */
            val area_limit = 0
            val ban_area_show = 0
            val follow = 0
            val follow_status = 0
            val login = 0
            val pay = 0
            val pay_pack_paid = 0
            val sponsor = 0
        }

        @Serializable class AreasBean {
            /**
             * id : 1
             * name : 中国大陆
             */
            val id = 0
            val name: String = ""
        }

        @Serializable class EpisodesBean {
            /**
             * aid : 476022007
             * badge :
             * badge_info : {"bg_color":"#FB7299","bg_color_night":"#BB5B76","text":""}
             * badge_type : 0
             * bvid : BV1iK41197nZ
             * cid : 927119874
             * cover : http://i0.hdslb.com/bfs/archive/9163ca2abcd22706bf055a04bcb4018e335fe253.jpg
             * dimension : {"height":2160,"rotate":0,"width":3840}
             * duration : 2244720
             * from : bangumi
             * id : 704473
             * is_view_hide : false
             * link : https://www.bilibili.com/bangumi/play/ep704473
             * long_title : 意义之塔
             * pub_time : 1670641200
             * pv : 0
             * release_date :
             * rights : {"allow_demand":0,"allow_dm":1,"allow_download":1,"area_limit":0}
             * share_copy : 《三体》第1话 意义之塔
             * share_url : https://www.bilibili.com/bangumi/play/ep704473
             * short_link : https://b23.tv/ep704473
             * status : 2
             * subtitle : 已观看1.6亿次
             * title : 1
             * vid :
             */
            @JvmField
            val checkState = 0

            @JvmField
            val selected = 0
            val aid: Long = 0

            @JvmField
            val badge: String = ""
            val badge_info: BadgeInfoBean = BadgeInfoBean()
            val badge_type = 0
            val bvid: String = ""
            val cid: Long = 0
            val cover: String = ""
            val dimension: DimensionBean = DimensionBean()
            val duration = 0
            val from: String = ""
            val id: Long = 0
            val isIs_view_hide = false
            val link: String = ""

            @JvmField
            val long_title: String = ""
            val pub_time = 0
            val pv = 0
            val release_date: String = ""
            val rights: RightsBeanX = RightsBeanX()
            val share_copy: String = ""
            val share_url: String = ""
            val short_link: String = ""
            val status = 0
            val subtitle: String = ""
            val title: String = ""
            val vid: String = ""

            @Serializable class BadgeInfoBean {
                /**
                 * bg_color : #FB7299
                 * bg_color_night : #BB5B76
                 * text :
                 */
                val bg_color: String = ""
                val bg_color_night: String = ""
                val text: String = ""
            }

            @Serializable class DimensionBean {
                /**
                 * height : 2160
                 * rotate : 0
                 * width : 3840
                 */
                val height = 0
                val rotate = 0
                val width = 0
            }

            @Serializable class RightsBeanX {
                /**
                 * allow_demand : 0
                 * allow_dm : 1
                 * allow_download : 1
                 * area_limit : 0
                 */
                val allow_demand = 0
                val allow_dm = 0
                val allow_download = 0
                val area_limit = 0
            }
        }

        @Serializable class SeasonsBean {
            /**
             * badge : 出品
             * badge_info : {"bg_color":"#00C0FF","bg_color_night":"#0B91BE","text":"出品"}
             * badge_type : 1
             * cover : http://i0.hdslb.com/bfs/bangumi/image/9870f898b8a39bbb8048f34317f8d78a02cc1770.png
             * horizontal_cover_1610 : http://i0.hdslb.com/bfs/bangumi/image/d236dc9c7aab08d77182a5ff3b2dc1aaf5c76217.png
             * horizontal_cover_169 : http://i0.hdslb.com/bfs/bangumi/image/6f12d97edbad9cf109b0bba250e6c84f13b30bdb.png
             * media_id : 4315402
             * new_ep : {"cover":"http://i0.hdslb.com/bfs/archive/487cbe9ca8f35dc043a907c5c81097c1ec3da7db.jpg","id":704475,"index_show":"更新至第3话"}
             * season_id : 26257
             * season_title : 第一季
             * season_type : 4
             * stat : {"favorites":6512751,"series_follow":6521332,"views":161123948}
             */
            val badge: String = ""
            val badge_info: BadgeInfoBeanX = BadgeInfoBeanX()
            val badge_type = 0
            val cover: String = ""
            val horizontal_cover_1610: String = ""
            val horizontal_cover_169: String = ""
            val media_id = 0
            val new_ep: NewEpBeanX = NewEpBeanX()
            val season_id = 0
            val season_title: String = ""
            val season_type = 0
            val stat: StatBeanX = StatBeanX()

            @Serializable class BadgeInfoBeanX {
                /**
                 * bg_color : #00C0FF
                 * bg_color_night : #0B91BE
                 * text : 出品
                 */
                val bg_color: String = ""
                val bg_color_night: String = ""
                val text: String = ""
            }

            @Serializable class NewEpBeanX {
                /**
                 * cover : http://i0.hdslb.com/bfs/archive/487cbe9ca8f35dc043a907c5c81097c1ec3da7db.jpg
                 * id : 704475
                 * index_show : 更新至第3话
                 */
                val cover: String = ""
                val id = 0
                val index_show: String = ""
            }

            @Serializable class StatBeanX {
                /**
                 * favorites : 6512751
                 * series_follow : 6521332
                 * views : 161123948
                 */
                val favorites = 0
                val series_follow = 0
                val views = 0
            }
        }

        @Serializable class SectionBean {
            val attr = 0
            val episode_id = 0
            val id = 0
            val title: String = ""
            val type = 0
            val episodes: List<EpisodesBeanX> = emptyList()

            @Serializable class EpisodesBeanX {
                /**
                 * aid : 648575736
                 * badge :
                 * badge_info : {"bg_color":"#FB7299","bg_color_night":"#BB5B76","text":""}
                 * badge_type : 0
                 * bvid : BV1pe4y1T7QU
                 * cid : 919377877
                 * cover : http://i0.hdslb.com/bfs/archive/db97e815b045169468d43283cd2b54ffbc8b3557.jpg
                 * dimension : {"height":1080,"rotate":0,"width":1920}
                 * duration : 116000
                 * from : bangumi
                 * id : 704787
                 * is_view_hide : false
                 * link : https://www.bilibili.com/bangumi/play/ep704787
                 * long_title : 纯享版
                 * pub_time : 1670727600
                 * pv : 0
                 * release_date :
                 * rights : {"allow_demand":0,"allow_dm":1,"allow_download":1,"area_limit":0}
                 * share_copy : 《三体》OP 纯享版
                 * share_url : https://www.bilibili.com/bangumi/play/ep704787
                 * short_link : https://b23.tv/ep704787
                 * stat : {"coin":7039,"danmakus":442,"likes":25510,"play":1513633,"reply":1566}
                 * status : 2
                 * subtitle : 已观看1.6亿次
                 * title : OP
                 * vid :
                 */
                val aid: Long = 0
                val badge: String = ""
                val badge_info: BadgeInfoBeanXX = BadgeInfoBeanXX()
                val badge_type = 0
                val bvid: String = ""
                val cid: Long = 0
                val cover: String = ""
                val dimension: DimensionBeanX = DimensionBeanX()
                val duration = 0
                val from: String = ""
                val id = 0
                val isIs_view_hide = false
                val link: String = ""
                val long_title: String = ""
                val pub_time = 0
                val pv = 0
                val release_date: String = ""
                val rights: RightsBeanXX = RightsBeanXX()
                val share_copy: String = ""
                val share_url: String = ""
                val short_link: String = ""
                val stat: StatBeanXX = StatBeanXX()
                val status = 0
                val subtitle: String = ""
                val title: String = ""
                val vid: String = ""

                @Serializable class BadgeInfoBeanXX {
                    /**
                     * bg_color : #FB7299
                     * bg_color_night : #BB5B76
                     * text :
                     */
                    val bg_color: String = ""
                    val bg_color_night: String = ""
                    val text: String = ""
                }

                @Serializable class DimensionBeanX {
                    /**
                     * height : 1080
                     * rotate : 0
                     * width : 1920
                     */
                    val height = 0
                    val rotate = 0
                    val width = 0
                }

                @Serializable class RightsBeanXX {
                    /**
                     * allow_demand : 0
                     * allow_dm : 1
                     * allow_download : 1
                     * area_limit : 0
                     */
                    val allow_demand = 0
                    val allow_dm = 0
                    val allow_download = 0
                    val area_limit = 0
                }

                @Serializable class StatBeanXX {
                    /**
                     * coin : 7039
                     * danmakus : 442
                     * likes : 25510
                     * play : 1513633
                     * reply : 1566
                     */
                    val coin = 0
                    val danmakus = 0
                    val likes = 0
                    val play = 0
                    val reply = 0
                }
            }
        }
    }
}
