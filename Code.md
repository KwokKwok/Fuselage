# Vue-router

重点：

1. `<router-link to="path">链接名字</router-link>`
1. `<router-view>`
1. `components`目录下添加组件模板
1. 修改`router/index.js`
    1. `children`数组

参数传递：

1. 通过`{{$route.name}}`可以访问路由页面的`name`属性
1. 通过`params`对象，注意`:to`是绑定语法
    ```html
    <router-link :to="{name:'route-a',params:{username:'郭垒'}}">To RouteA</router-link>`
    ```
    ```html
    <!--Vue文件内。-->
    <template>
        <div>
            <p>{{$route.params.username}}</p>
        </div>
    </template>
    ```
1. 通过URL传值
    ```html
    <router-link to="/pages/1234/gl" >页面A</router-link>
    ```
    ```js
    //router/index.js中设置页面路径
    //path使用':'加参数名，可有多个参数
    path: ':id(\\d+)/:name'
    ```
    ```html
    <!--页面的.vue文件-->
    <template>
        <div>
            <p>{{$route.params.id}}</p>
            <p>{{$route.params.name}}</p>
        </div>
    </template>
    ```

单页面多**路由区域**

> 再理解理解

```html
<router-view/>
<router-view name="route1"/>
<router-view name="route2"/>
```

```js
components: {
    default: Hi,
    route1: routeA,
    route2: routeB
}
```

## 重定向和别名

> [文档](https://router.vuejs.org/zh-cn/essentials/redirect-and-alias.html)

### redirect 重定向

1. 基本重定向，在`router/index.js`中写路由时，将`components`替换为`redirect：'path'`
1. 重定向传参，可以利用`URL`的方式：
    ```js
    {
        //以to访问的时候，可以获取到number值
        path: 'redirect/:number(\\d+)',
        name: 'redirect',
        //以number值为路径做重定向，会重定向到下面的IdPage
        redirect: ':number'
    }, {
        path: ':id(\\d+)',
        component: IdPage
    }
    ```

### alias 别名

```js
routes: [
    { path: '/a', component: A, alias: '/b' }
]
```

/a 的别名是 /b，意味着，当用户访问 /b 时，URL 会保持为 /b，但是路由匹配则为 /a，就像用户访问 /a 一样。

同样是页面改变：

- 重定向会修改路径
- 别名路径不变

## 路由的mode和404页面

路由mode：

- `history`，不显示`#`
- `hash`，显示`#`

```js
export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    }
  ]
})
```

404页面：添加一个路由项，`path: '*'`即可。

## 路由钩子函数

### 在路由注册文件中写

只能写`beforeEnter`

```js
export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld,
      beforeEnter(to, from, next) {
        console.log(to); //to是一个对象，这里是当前的
        console.log(from); //from也是一个对象，指示从哪来的
        console.log(next); //next是一个函数，指示是否跳转
        next(); //跳转，如果不写相当于next(false)，不跳转
      }
    },
})
```

### 在路由页面写

比如`Hi.vue`:

```js
<script>
export default {
  name: "Hi",
  data() {
    return {
      msg: "Hello router"
    };
  },
  beforeRouteEnter(to, from, next) {
    console.log(to, from, next);
    next();
  },
  beforeRouteLeave(to, from, next) {
    console.log(to, from, next);
    next();
  }
};
</script>
```

注意：都是写了`next()`才能正常跳转。

## 代码跳转

```js
<script>
export default {
  name: "App",
  methods: {
    go() {
      //向前
      this.$router.go(1);
    },
    back() {
      //向后
      this.$router.go(-1);
    },
    home() {
      //跳转到指定页
      this.$router.push("/");
    }
  }
};
</script>
```
