# SegmentForkJoinTask

#### Description
具备指定分段大小自动处理分段的ForkJoinTask，例如有一个数据：[0, 1000]，现要求，使用4个线程处理分段处理，那么使用此库后会自动的创建4个线程分别处理[0, 250]、[250, 500]、[500, 750]、[750, 1000]，你只需要指定每个线程中需要做什么事情即可。当然还支持更复杂的处理比如一次处理这样的不连续数据：[[0, 1432], [2048, 3100], [3249, 14576]]自动均衡分段等。

#### Software Architecture
Software architecture description

#### Installation

1.  xxxx
2.  xxxx
3.  xxxx

#### Instructions

1.  xxxx
2.  xxxx
3.  xxxx

#### Contribution

1.  Fork the repository
2.  Create Feat_xxx branch
3.  Commit your code
4.  Create Pull Request


#### Gitee Feature

1.  You can use Readme\_XXX.md to support different languages, such as Readme\_en.md, Readme\_zh.md
2.  Gitee blog [blog.gitee.com](https://blog.gitee.com)
3.  Explore open source project [https://gitee.com/explore](https://gitee.com/explore)
4.  The most valuable open source project [GVP](https://gitee.com/gvp)
5.  The manual of Gitee [https://gitee.com/help](https://gitee.com/help)
6.  The most popular members  [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
