# MagicMessenger
## 安卓跨进程跨app通信框架

本框架实现了安卓跨进程跨app通信的方便调用和封装

### 使用方法:

第一步:

在 application 里面初始化
```
MagicMessenger.init(this); //跨进程需要
MagicMessenger.bindOtherAPP(this, "包名"); //跨app需要,不跨app可以去掉
```
> 跨app需要主app初始化跨进程功能
#### 注意!初始化代码需要在每个进程都初始化,所以不要加进程判断代码

第二步:

在需要接受消息的对象中,订阅消息,第一个参数,是接收消息的标志,需要唯一;否则可能会收不到消息!                
消息依赖bundle传递,可传递类型跟bundle 允许类型一致
```
MagicMessenger.subscribe("key", new MessageCallback() {
            @Override
            public void onMsgCallBack(Bundle data) {
                
            }
        });
```
发送消息示例:
```
	Bundle bundle = new Bundle();
        bundle.putString("test", "activity1 发送消息到服务");
        MagicMessenger.post("key", bundle); //第一个参数为消息订阅标志,需要唯一
```
第三步:

在对象销毁时,取消订阅,否则会导致内存泄漏

参数key 是订阅时候的唯一标识
```
MagicMessenger.unsubscribe("key");
```

### 依赖方法:

1.在全局build里添加仓库:
```
allprojects {
            repositories {
	            ......
	            maven { url 'https://jitpack.io' }
	}
}
```

2.在app的build里添加依赖:
```
dependencies {
            ......
            implementation 'com.github.jarryleo:MagicMessenger:v2.0'
}
```
