# SegmentForkJoinTask

#### 介绍
具备指定分段大小自动处理分段的ForkJoinTask，例如有一个数据：[0, 1000]，现要求，使用4个线程处理分段处理，那么使用此库后会自动的创建4个线程分别处理[0, 250]、[250, 500]、[500, 750]、[750, 1000]，你只需要指定每个线程中需要做什么事情即可。当然还支持更复杂的处理比如一次处理这样的不连续数据：[[0, 1432], [2048, 3100], [3249, 14576]]自动均衡分段等。

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
