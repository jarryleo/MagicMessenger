# Android轻松实现跨进程/跨app通讯框架及其原理

先给没耐心的朋友上酸菜:
[框架GitHub](https://github.com/jarryleo/MagicMessenger)    
如果觉得好用,希望给个star支持一下

现有跨进程方案:
    
    - aidl
    - Messenger
    - broadcast
    - socket
    
以上实现都很繁琐     
现基于Messenger 封装一个跨进程跨app通讯框架    
关于Messenger :     
    可以先看这篇博客:
    [Android 基于Message的进程间通信 Messenger完全解析](https://blog.csdn.net/lmj623565791/article/details/47017485)
    
### 先简单介绍下Messenger的原理:

   Messenger是系统基于aidl封装的一个简易的 通过 handler 传输数据
   跨进程通信框架;
   创建简单服务端:
   
    ```
        mMessenger = new Messenger(handler) 
    ```

   在服务里面返回它的binder即可:
   
    ```
        @Override
        public IBinder onBind(Intent intent)
        {
            return mMessenger.getBinder();
        }
    ```
    
   在handler 的 handleMessage 即可拿到对面进程的 Messenger
   
    ```
    static class ServiceHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                Messenger client = msg.replyTo;
            }
        }
    ```
    
   简单客户端:
   
    ```
     mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    ```
    
   对,绑定服务端的服务即可
    
    ```
    static class ClientConn implements ServiceConnection {
    
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mServer = new Messenger(service);
                reg();
            }
    
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServer = null;
                connect();
            }
        }
    ```
   mServer 即为服务端 的 Messenger 对象;  
    通过它就可以把消息发到服务端   
    服务端在handler里收到消息,同时拿到 客户端的 Messenger
    
   客户端也要 new 一个 Messenger
   
    ```
    private static ClientHandler handler = new ClientHandler();
    private static Messenger mMessenger = new Messenger(handler);
    ```
   Messenger 里包含客户端的handler:
    用来处理接受到的服务端发送来的消息
    
    ```
    static class ClientHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                //这里是服务端发来的消息
               
            }
        }
    ```
    
   客户端必须主动向服务端发送消息,同时把自己的Messenger 放到消息里面发送给服务端
    
    ```
    public void sendMsg(Bundle bundle) {
            Message msg = Message.obtain(handler, Constant.SEND_MSG_TO_TARGET);
            msg.setData(bundle);
            msg.replyTo = mMessenger; //把客户端自己的Messenger 放到消息里面发送给服务端
            try {
                if (mServer != null) {
                    mServer.send(msg);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    ```
   这样,服务端拿到了客户端的Messenger
    
   它们互相持有对方的 Messenger 就可以做到跨进程的双向通信!
    
   > 注意:Message 为handler的消息对象,但是不能使用它的obj传递消息
   > 只能用它的 what,arg1,arg2和setData(bundle)传递消息,否则会报:
   > 不可跨进程传递非序列化对象错误,即时obj传递的是一个序列化对象
    
## 下面来介绍框架实现思路:
    
   上面这类互相持有对方内部对象的现象是不是跟 接口回调非常类似!
    
   既然做到了接口回调,那么观察者模式还会远吗?
    
   对的,如果服务端通过map持有多个客户端的Messenger 那不就实现了
   观察者模式吗?
    
   想到这里相比就很简单了
    
我一开始的思路是,在主进程创建服务;用这个服务持有其他需要通讯的客户端的
Messenger;
其他通讯的客户端每个对象创建一个 Messenger;
但是我发现在服务端给每个客户端保存对象的map集合不好区分每个对象的key
因为我用的key是Integer;
后来我采用的办法是获取客户端进程的进程名,然后拿进程名的hashCode作为key
这样每个进程只有一个Messenger对象,但是每个进程可能有多个消息传递
所以再给每个进程创建一个消息中心
把每个需要接收消息的对象在消息中心订阅;
然后服务端每次接收到消息都转发给所有客户端Messenger

客户端Messenger再在消息中心遍历接收对象订阅消息时候的key,把消息发送到指定对象
框架基本原理说清楚了!

### 下面说说跨app通信

原理:
通过aidl 的跨app通信,其实就是绑定另一个app的远程服务:   
本框架实现原理是一样的:   
框架用变量${applicationId}表示包名;
这样包名就是使用本框架的app包名,达到框架在任意项目使用的目的;

```
<service
      android:name="cn.leo.messenger.BinderPool"
      android:exported="true">
      <intent-filter>
          <action android:name="${applicationId}.messenger"/>
      </intent-filter>
</service>
```

```
Intent intent = new Intent(mPkgName + ".messenger");
intent.setPackage(mPkgName);
        
mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
```
    
剩下的操作就和上面跨进程一样一样的了;都是绑定服务而已

 ### 框架使用方法:
 
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
    