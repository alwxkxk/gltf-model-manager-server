

## 开发说明
- 本项目使用lombok，开发时IDE需要安装对应插件。
- 数据库：MySQL 8
- java版本： openjdk 17
- 配置信息在 `resources/application.properties`
- 数据库配置信息需手动创建配置：`src/main/resources/db.properties`，模板可参考`db_example.properties`。



## 业务逻辑
前端3D场景编辑，是通过model_info的name进行绑定。每个模型加载时也先通过name找到当先使用版本的model入口文件进行加载。


模型管理：
先选择现在模型信息，或创建新的模型信息。
选择新的模型文件，前端处理好图片尺寸、质量，以及缩略图，检测hash冲突等问题。
上传之前选择版本号，打包压缩后上传，后端解压后创建相关file、model数据，返回上传成功。
删除模型时，对应的model_info_version会马上删除，而文件与模型是懒删除标记，不会真的删除，会移到回收站里显示，一个月后才真的删。恢复时要选择恢复于哪个只需要把model_remove数据删除，把相关模型的懒删除标记设置回来，根据version恢复回去。








## 数据表设计
前端选择上传模型文件gltf(separate)，一个模型对应一个文件夹。
每上传一个新模型(model)，都会创建一个文件夹来存放文件(file)。
下载某个模型全部文件时，是根据model_id找到对应的directory_path，压缩整个文件夹进行下载。
前端加载模型时，根据model_id找到模型的入口文件路径与附属文件夹路径，其入口文件及附属文件都通过传path来进行下载。

每个模型信息(model_info)可包含多个版本(model_info_version)，每个版本对应一个模型(model)。
每个模型信息(model_info)可包含多个标签(model_info_tag).

程序开始运行时，会先检测数据库表格字段是否与代码一致:
https://stackoverflow.com/questions/11729828/jdbc-java-how-to-check-if-a-table-and-also-a-column-exist-in-a-database



### file
每个文件的信息。

columns:
- file_id 文件id(uuid)
- model_id 模型id
- path 文件下载完整路径
- hash 文件哈希值
- lazy_remove 懒删除标记
- created_time 创建时间
- last_download_time 最后的下载时间：用于区分是否存在太久没下载的文件。同时如果已经被标记懒删除还在下载，要发出告警，说明异常。

indexes:
- file_id 主键
- path 索引
- hash 索引
### model
用于定位某个模型所对应的文件夹。

columns:
- model_id 模型id(uuid)
- model_file_path 模型入口文件的下载路径
- directory_path 该模型对应的文件夹路径,gltf加载完入口文件后解析加载其它附属文件时需要用到。
- lazy_remove 懒删除标记
- created_time 创建时间

indexes:
- model_id 主键
### model_info
模型信息用于管理模型的版本，创建用户等

columns:
- model_info_id 模型信息id(uuid)
- name 模型名称，唯一且不可再修改删除
- user_id 所属用户id
- use_model_id 当前使用版本的模型id
- note 备注说明
- created_time 创建时间

indexes:
- model_info_id 主键
- name 唯一且不可再修改
### tag
columns:
- tag_name 标签名
- count 数量

indexes:
- tag_name 主键

### model_info_tag
模型信息对应的标签。

columns:
- model_info_id 模型信息id
- tag_name 标签名

indexes:
tag_id与model_info_id 联合唯一索引

### model_info_version
模型信息所对应的版本，一对多。

columns:
- model_info_id 模型信息id
- version 版本号
- model_id 对应的模型id
- note 备注说明
- created_time 创建时间

indexes:
model_info_id与version 联合唯一索引

### model_remove
- remove_time 删除时间
- model_info_id 模型信息id
- version 版本号 恢复时，版本号会变成 `${version}_recover_${remove_time}`，需要用户自行修改版本号名称
- model_id 模型id