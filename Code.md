# 饥人谷三小时

> [前端三小时速成指南](https://xiedaimala.com/courses/b8b4c00c-6798-4caf-8bfe-ba9fbb4c6d3d)

## 声明前置

进入**新的执行环境**的时候，会将`var`、`function`声明的变量前置（*注意：只是声明，未赋值*），之后才开始执行代码。

---

## 引用类型

> 其实赋值和拷贝都是值传递，只是引用类型变量的值是地址引用。

`===`判断引用类型时注意，引用类型变量存放的是堆中数据的引用地址。所以，即使看起来两个对象的内容一样，两个变量也不相等。

---

## 函数作用域链

- 执行的过程中，先从自己内部找变量
- 如果找不到，再从**创建当前函数**所在的作用域出发，不断往上去找

---

## 闭包

> 闭包，是函数和**被创建时**所在的词法环境的组合。

```js
function person(name){
    return function(){console.log(`${name} is handsome!`)}
}

var a = person("A")
var b = person("B")

a()
b()
```

```Console
A is handsome!
B is handsome!
```

---

## 跨域

- 同协议
- 同域名
- 同端口

> 1. js发送ajax请求不同域。此处是由浏览器做的安全限制。就是说你的请求是可以发出去并得到回应，但是被浏览器拦截掉。
> 1. 另外还有一种`iframe`不同域，浏览器会禁止页面获取或操作`iframe`里的DOM。这种情况下可以通过降域或者是`PostMessage`

1. `JSONP`，需要后端配合包装数据。利用`script`标签发起请求，返回的内容，不再是数据，而是一个包含数据的方法调用。
1. `CORS`，浏览器会将请求加上`Origin`请求头，后台会对其进行一系列处理，如果确定接受该请求，会再返回结果添加一个响应头`Access-Control-Allow-Origin`。

---

## 面向对象

```js
function People(name){
    this.name=name;
}

People.prototype.sayName = function(){
    console.log(`My name is ${this.name}`)
}

var p = new People('Daxing')
p.sayName()
```

`new`+函数A执行，会：

1. 创建一个空对象，对象的`__proto__`属性指向`A`的`prototype`(*所以`__proto__`这个属性和自身的`prototype`不一样，可以看作是作用域链中自身的上一级的`prototype`*)
1. 如果返回值是基本类型，则会被忽略。
1. 自身找不到的，会沿着`__proto__`向上找。

---

## this

总体来说，是谁调用，this指向谁。
但是对于一个直接的方法调用：

1. 严格模式下，this相当于undefined
1. 非严格模式下，会自动将`undefined`和`null`转换为对`window`的引用

`array[0]=function(){this}`，调用`array[0]()`，this指代array

`func.bind(obj)`，则`func`内的`this`会指向`obj`

箭头函数内是没有`this`的，只能借用上层的`this`

---

## 继承

```js
function Dialog(target) {
    this.target = target
}
Dialog.prototype.show = function() {
    console.log(this.target + ' show')
}
Dialog.prototype.hide = function() {
    console.log(this.target + ' hide')
}
var dialog = new Dialog('body')
dialog.show()

function Message(target, name) {
    Dialog.call(this, target)     //这句很重要
    this.name = name
}
Message.prototype = Object.create(Dialog.prototype)   //这句更重要
Message.prototype.success = function() {
    console.log(this.name + ' success' )
}

var msgBox = new Message('main', 'msg')
msgBox.show()
msgBox.success()
```

![继承原型图](https://cloud.hunger-valley.com/17-12-8/16780872.jpg)

`Dialog.call(this, target)`的作用是: 执行函数 `Dialog(target)`，执行的过程中里面遇到 `this` 换成当前传递的 `this`

`Object.create(Dialog.prototype)`的作用是：创建一个空对象，空对象的`__proto__`等于 `Dialog.prototype`

---

## Cookie和Session

> 存储在浏览器的一些数据，根据设置的不同，在刷新或者浏览器关闭后依然存在。可以使用`document.cookie`查看正在浏览的网站的cookie

使用方式：

1. 通过js代码，保存一些不重要的信息。（*使用较少*）
1. 更常见的是：服务端通过HTTP协议规定的`Set-Cookie`来让浏览器种下cookie。

一般浏览器cookie最大为4k，每次网络请求，都会带上合适的cookie。所以也会对传输效率有影响。（*静态资源使用CDN，这样请求静态资源的时候，就不会携带上页面的cookie了，这也是一个好处*）

cookie参数：

- `path`，发挥作用的路径。匹配路径才会发送cookie
- `expires`和`maxAge`，指示cookie什么时候过期。一个是绝对的时间，一个是相对的时间。不指定时，会产生session cookie，用户关闭浏览器即被清除，一般用于保存session的session_id。
- `secure`，指示是否仅适用于`HTTPS`
- `httpOnly`，为True时，浏览器不允许脚本操作修改cookie。一般都应该设置为true，避免xss攻击拿到cookie。

session机制：

1. 用户登陆
1. 服务器验证通过，产生一个session保存起来，将session_id通过`Set-Cookie`保存到用户浏览器。
1. 用户刷新，发起网络请求并携带Cookie
1. 服务器通过`session_id`识别到用户。

---

## Web安全

1. 传输安全
1. 浏览器安全
1. 传统攻击

### 传输安全

使用HTTP是不安全的。传输过程中可以对你的数据做任何修改。

而使用HTTPS是安全的，底层使用非对称加密。但申请HTTPS需要在国际认证的CA机构填写网站信息，而这些CA机构也是有上层的CA机构。这样，浏览器验证一个网站时通过CA链逐级验证，浏览器内置根CA和顶级CA的证书。也可以自己伪造HTTPS，但不会通过验证。

### XSS攻击

1. 用户输入做验证。
1. 慎重用`eval`。

### CSRF

1. 提交字段的时候多加一个Token
1. 服务器生成一个字段，嵌入到一个隐藏的输入框内。这时候，提交的时候验证字段。

---

## 前端性能优化

1. 加载
1. 体验

### 加载

1. 网络
    - 接入高带宽的网络
    - 静态资源放CDN
1. 数据体积小
    - 服务端压缩
    - 服务端接口多样
    - 图片压缩、多样性
    - css等压缩
1. 数据量小
    - 资源合并，减少请求
1. 服务器处理速度
1. 重复利用
    - 公用资源合并
    - 缓存

### 体验

1. 懒加载，只加载当前必要的东西。
    - 先把页面加载出来，然后再加载别的资源和逻辑。
    - 轮播只预先加载一个图片。
1. 不要卡顿。
    - 动画不要卡，优先使用CSS3的动画。使用3D可以调用GPU。位移使用Transform>Left>margin

---

## HTTP

1. HTTP报文
1. HTTP状态码
1. HTTP缓存

### HTTP报文

1. 对报文进行描述的起始行
    - 请求体首行`GET \ HTTP/1.1` -> `<method><request-URL><version>`
    - 响应体首行`HTTP/1.0 200 OK` -> `<version><status><reason-phrase>`
1. 包含属性的首部块
    - 通用首部
    - 请求特有的首部
    - 响应特有的首部
1. 可选的包含数据的主体
1. **请求**方法：
    - `GET`，获取资源
    - `HEAD`，不获取资源，只获取头部。主要用于获取相关信息，比如缓存时查看内容是否被修改。
    - `PUT`，和`GET`相反，想要往服务器写入信息。类似于修改。
    - `POST`，向服务器发送数据。
    - `TRACE`，测试路由
    - `DELETE`，删除。
    - `OPTIONS`，请求服务器告知支持的功能。

### 状态码

1. 100-199，http1.1新增的。指示客户端做出反应
1. 200-299，请求成功
1. 300-399，资源重定位
1. 400-499，客户端错误，比如400请求语法错误，404没有请求资源
1. 500-599，服务器错误。

### 缓存

1. `Expires`，客户端和服务器可能时间不一致，可使用`maxAge`控制。
1. `Cache-Control`
    - `Public`，响应可被任何中间节点缓存
    - `Private`，不允许中间节点缓存。
    - `no-cache`，不适用`Cache-Control`，可能使用别的机制。
    - `no-store`，不做缓存
    - `max-age`，表示当前资源有效时间，单位为秒。
1. `ETag`，比如发起请求之后，会返回对文件的编码信息比如`ETag`。只要文件不改变，`ETag`就不会改变。这样，浏览器发请求的时候带上`ETag`，服务器检查`ETag`是否一致。一致则表示文件未修改，返回不含资源的短消息 比如`304`（*缓存文件仍可用*），如果不一样则返回新的资源和新的`ETag`。
1. `Last-Modified/If-Modified-Since`类似`ETag`，缺点只能精确到秒，优点是消耗较小。机制一致。
