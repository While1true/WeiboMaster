# WeiboMaster
kotlin 开始
## 搭建一体式多方位应用

---
-  前言：以下的内容都来自一位佛学师父的相关微博。
-  由于微博的分类搜索分类不是很明确，不易找到想要的内容，于是便起了自己做一个。一方面锻炼技术，一方面也许分享给同样想自己整应用的人
-  自从学习的python语言，不得不叹服于它的简洁和强大，本应用的后台全基于python
---
### One.先看效果
#### 1.Android端

>分类页
![device-2018-05-21-105635.png](https://upload-images.jianshu.io/upload_images/6456519-3342b37984f15e12.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 基本分类信息检索页

![device-2018-03-29-084157.png](https://upload-images.jianshu.io/upload_images/6456519-4be51fad234da4ce.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)

> 基于wordcould的动态云图

![device-2018-03-29-084240.png](https://upload-images.jianshu.io/upload_images/6456519-d21bbc68407db7a3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)


> X5webview 的原博客

![device-2018-03-29-084303.png](https://upload-images.jianshu.io/upload_images/6456519-1d1821d0a660bf72.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)

> 基于virtualApk的插件化小程序

![device-2018-03-29-084319.png](https://upload-images.jianshu.io/upload_images/6456519-134ee6e15c21fd39.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)

#### 2.1电脑网页端
![图片.png](https://upload-images.jianshu.io/upload_images/6456519-c69e81dbc77d4ca4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/600)
##### 2.2手机网页端
![device-2018-03-29-085533.png](https://upload-images.jianshu.io/upload_images/6456519-2cc8666928b332f7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)

#### 3.后台管理
![图片.png](https://upload-images.jianshu.io/upload_images/6456519-131ae3f7c0f8e560.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/600)

插件化管理后台

![图片.png](https://upload-images.jianshu.io/upload_images/6456519-740c115c163c9ce0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/600)

### Two.整体实现


> #### 1.服务端python+Django+mysql+wordcould+...
> #### 2.网页端 python + html模板 + js+css+...

> #### 3.android端kotlin+rxjava+okhttp+virtualApk+X5webview+html+一系列自定义控件，开源框架+...

### Three.实现思路
- ##### 1.利用 python+正则表达式+beautifulsoup+多线程+pymsql 将博客指定的内容自动化获取到本地数据库
>  由于选取的是从pc端博客获取，中间遇到了很棘手的数据结构问题。先要分析网页的内容结构，包括加载更多和分页加载的网址情况，内容实体的结构和不同结构下获取指定内容，不同数据情况的异常分类处理。要善于利用正则表达式，获取想要的部分，然后处理掉\r\t\n一些列干扰字符，构成html，用beautifulsoup解析，存储到数据库

---
- ##### 2.利用python+django 搭建服务器，实现后台管理，用于可视化管理数据
> 具体实现情况详见django官网教程

- ##### 3.搭建Android端框架对接服务器
- ##### 4.寻找网页博客模板对接服务器

### Four.需求分析确定
> 1.想打造一个 分好类，便于检索，可以收藏，分享的Android App

> 2.如上的网页端
  

---
> 具体实现
1. 数据GET：
-   1.确定默认分类
-   2.利用python 检索网页根据不同的关键词分类自动写入数据库
2. Andoird 搭建
-   1.利用最新的kotlin语言
-   2.基础组件工具的封装
-   3.自定义上拉刷新，自动分页加载，多功能Adapter，等一些列自定义控件
-   4.利用Rxjava+Retrofit+okhttp封装网络库
-   5.利用virtualApk实现插件化分步开发
-   6.记录ip和deviceid实现用户统计
-   7.集成x5webview加载网页
-   8.集成Glide rxpermission，Lifecycle...

2. 后台搭建
-   1.python+mysql+Django
-   2.wordcould+jieba生成云图返回客户端
-   3.基于Django QuerySet实现数据库查询、排序、切片，加工处理
-   4.文件的上传下载


2. 网页搭建
-   1.Django模板化语言，根据查询内容动态生成html

#### Five.开始动工
     走到这里就是每天一步步去实现啦！
[python2.7信息获取源码](https://github.com/While1true/PythonLearning)
[Android源码](https://github.com/While1true/WeiboMaster)
[python3.6服务端源码](https://github.com/While1true/PYServer)


博客信息获取的py文件在generater.py
![图片.png](https://upload-images.jianshu.io/upload_images/6456519-520c12c56ff5746f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


服务要跑起来
1.settings.py 的ip要加入你的ip
![图片.png](https://upload-images.jianshu.io/upload_images/6456519-7f35cdd6c48e2cfe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/600)


下标改为你插入的位置
![图片.png](https://upload-images.jianshu.io/upload_images/6456519-ef903abf26f583bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
进入 manage.py 将服务开启来。需要自己去学习下python Django

Android 端 改ip为你的ip 启动端口
![图片.png](https://upload-images.jianshu.io/upload_images/6456519-b679e88119db12df.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/600)

> 我没有运行在云平台，以下点击是打不开的

后台入口 你的ip+端口+admin（http://10.0.110.134:8090/admin/）

网页入口你的ip+端口+应用名+index（http://10.0.110.134:8090/masterWeiBo/index）

所有数据库：（http://note.youdao.com/noteshare?id=f98dfc8417a2ae7d1990343e387e87b6）


