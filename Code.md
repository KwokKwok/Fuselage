## Vue选项

### propsData，Vue.extend的数据传递

先在HTML里写一个自定义标签

```html
<gl></gl>
```

写一个扩展，注意写上`props`（*数组*），写上我们要用到的属性。

然后挂载，注意构造的时候传的是一个对象，对象里需要再声明一个`propsData`的对象，在`propsData`里写上我们要设置的属性和值。

```js
var gl = Vue.extend({
  template: `<h1>{{msg}}</h1>`,
  props: ['msg']
})

new gl({ propsData: { msg: '郭垒很帅' } }).$mount('gl');
```

结果：

```html
<h1>郭垒很帅</h1>
```

### computed，计算属性

> [计算属性](https://cn.vuejs.org/v2/guide/computed.html#%E8%AE%A1%E7%AE%97%E5%B1%9E%E6%80%A7)

划重点：

1. 计算属性使用起来和普通属性一致
1. 计算属性会随着被其依赖的属性的改变而改变。
    > 阅读理解：*"我们已经以声明的方式创建了这种依赖关系：计算属性的 getter 函数是没有副作用 (side effect) 的，这使它更易于测试和理解。"*
1. 计算属性是有缓存的，只有在依赖改变的时候才会重新求值。如果没有依赖**响应式依赖**，则计算属性的值不会更新。[计算属性缓存 vs 方法](https://cn.vuejs.org/v2/guide/computed.html#%E8%AE%A1%E7%AE%97%E5%B1%9E%E6%80%A7%E7%BC%93%E5%AD%98-vs-%E6%96%B9%E6%B3%95)
1. 如果一些数据需要随着其他数据的变化而变化，请使用计算属性，而不要使用`watch`。[计算属性 vs 侦听属性](https://cn.vuejs.org/v2/guide/computed.html#%E8%AE%A1%E7%AE%97%E5%B1%9E%E6%80%A7-vs-%E4%BE%A6%E5%90%AC%E5%B1%9E%E6%80%A7)
1. 计算属性默认只有`getter`，如果需要，也可以提供`setter`
    ```js
    computed: {
        fullName: {
            // getter
            get: function () {
                return this.firstName + ' ' + this.lastName
            },
            // setter
            set: function (newValue) {
                var names = newValue.split(' ')
                this.firstName = names[0]
                this.lastName = names[names.length - 1]
            }
        }
    }
    ```

简单示例如下：

```html
<h1>{{msg}}</h1>
```

```js
data: {
    name: '郭垒'
},
computed: {
    msg() {
        return this.name + "很帅"
    }
}
```

### mothods，如何调用及传参

1. 普通调用，`@click='add'`
1. 获取原生事件，`@click='add'`，方法定义`add(enent){}`
1. 需要传递参数，`@click='add(3)'`，方法定义`add(num){}`
1. 需要传递参数同时也要获取事件，可以使用`$event`参数，`@click='add(3,$event)'`，方法定义`add(num,event){}`
1. 组件调用Vue实例的方法，`@click.native='add'`，如下：
    ```html
    <div id="app">
        <h1>{{count}}</h1>
        <btn @click.native="add" btn-name='Add'></btn>
    </div>
    ```
    ```js
    var btn = {
        template: `<button>{{btnName}}</button>`,
        props: ['btnName']
    }

    var app = new Vue({
        el: '#app',
        data: {
            count: 0,
        },
        methods: {
            add(event) {
                this.count += 1;
                console.log(event)
            }
        },
        components: {
            'btn': btn
        }
    })
    ```

### watch，侦听器

> [侦听器](https://cn.vuejs.org/v2/guide/computed.html#%E4%BE%A6%E5%90%AC%E5%99%A8)

基本用法就是在watch块内为你要观察的数据，写一个方法。

```js
var app = new Vue({
  el: '#app',
  data: {
    count: 0,
  },
  methods: {
    add() {
      this.count += 1;
    }
  },
  watch: {
    count() {
      console.log(this.count);
    }
  }
})
```

也可以写在Vue实例外部，使用`vm.$watch API`：

```js

var app = new Vue({
  el: '#app',
  data: {
    count: 0,
  },
  methods: {
    add() {
      this.count += 1;
    }
  },
})

//vm.$watch(prop,func)
app.$watch('count', () => { console.log(app.count) })
```

### mixins，混入

> 可以理解为通用方法提取。[Mixin文档](https://cn.vuejs.org/v2/guide/mixins.html)，*"混入对象可以包含任意组件选项。当组件使用混入对象时，所有混入对象的选项将被混入该组件本身的选项。"*

例子：

```js
// 定义一个混入对象
var myMixin = {
  created: function () {
    this.hello()
  },
  methods: {
    hello: function () {
      console.log('hello from mixin!')
    }
  }
}

// 定义一个使用混入对象的组件
var Component = Vue.extend({
  mixins: [myMixin]
})

var component = new Component() // => "hello from mixin!"
```

划重点：

1. 可以包含**任意组件选项**。意味着，你在构造Vue实例时使用的`data`、`methods`、以及`生命周期钩子函数`都可以使用。
1. 你写在Mixin对象中的选项**会添加到**使用该Minin对象的Vue实例的选项中。
1. 合并。
    - `data`，数据对象会重新进行组合，包含所有的数据。如果有冲突，保留组件(*Vue实例*)的数据
    - `钩子函数`，合并成数组，混入对象的钩子**在前**，依次被调用。
    - 值为对象的选项，`methods`、`components`和`directives`，会被混合成一个对象，冲突的属性，只保留组件的内容。逻辑和`data`类似。(*注意在两个`methods`里声明了同名方法，只会保留Vue实例的方法，而不是像钩子函数一样，都被调用*)
1. [全局混入](https://cn.vuejs.org/v2/guide/mixins.html#%E5%85%A8%E5%B1%80%E6%B7%B7%E5%85%A5)，会自动影响到所有之后创建的Vue实例。另外可参考[自定义选项合并策略](https://cn.vuejs.org/v2/guide/mixins.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E9%80%89%E9%A1%B9%E5%90%88%E5%B9%B6%E7%AD%96%E7%95%A5)
    ```js
    // 为自定义的选项 'myOption' 注入一个处理器。
    Vue.mixin({
        created: function () {
            var myOption = this.$options.myOption
            if (myOption) {
                console.log(myOption)
            }
        }
    })

    new Vue({
        myOption: 'hello!'
    })
    // => "hello!"
    ```

### extends，扩展

> 允许声明扩展另一个组件(可以是一个简单的选项对象或构造函数)，而无需使用 Vue.extend。这主要是为了便于扩展单文件组件。和`mixins`类似。

```js
var CompA = { ... }

// 在没有调用 `Vue.extend` 时候继承 CompA
var CompB = {
  extends: CompA,
  ...
}
```

### delimiters，配置插值形式

> 改变纯文本插入分隔符。只在完整构建版本中的浏览器内编译时可用。

比如，如果你想要替换`{{}}`为`${}`：

```js
new Vue({
  delimiters: ['${', '}']
})
```

## 其他

### 和jQuery一起使用。

引入jQuery之后，就可以和jQuery一起使用了。

另外在`.vue`文件使用jQuery：

1. `npm install jquery`
1. `import $ from 'jquery'`

### 生命周期相关的实例方法

> [实例方法 / 生命周期](https://cn.vuejs.org/v2/api/#%E5%AE%9E%E4%BE%8B%E6%96%B9%E6%B3%95-%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)

- [`vm.$mount(el)`](https://cn.vuejs.org/v2/api/#vm-mount)，如果已经设置了`el`，再调用`vm.$mount()`则不会有效果。*注：可以通过`vm.$el`访问实例的`el`属性*
- [`vm.$nextTick()`](https://cn.vuejs.org/v2/api/#vm-nextTick)，修改完数据后，DOM可能还没有更新。和全局方法`Vue.nextTick()`的区别是，可以使用`this`自动指向调用它的Vue实例。这个回调会在下**一次DOM更新**后调用。只是一次。
    ```js
    new Vue({
        // ...
        methods: {
            // ...
            example: function () {
            // 修改数据
            this.message = 'changed'
            // DOM 还没有更新
            this.$nextTick(function () {
                // DOM 现在更新了
                // `this` 绑定到当前实例
                this.doSomethingElse()
            })
            }
        }
    })
    ```
- [`vm.$destory()`](https://cn.vuejs.org/v2/api/#vm-destroy)，见名知意，一般用不到。
- [`vm.$forceUpdate()`](https://cn.vuejs.org/v2/api/#vm-forceUpdate)，强制刷新。

### 事件相关的实例方法

> [实例方法/事件](https://cn.vuejs.org/v2/api/#%E5%AE%9E%E4%BE%8B%E6%96%B9%E6%B3%95-%E4%BA%8B%E4%BB%B6)

- [`vm.$on(event,callback)`](https://cn.vuejs.org/v2/api/#vm-on)，监听当前实例的自定义事件，事件可以由`vm.$emit()`触发。事件参数会传入到回调函数中。
    ```js
    vm.$on('test', function (msg) {
        console.log(msg)
    })
    vm.$emit('test', 'hi')
    // => "hi"
    ```
- [`vm.$emit(event,[…args])`](https://cn.vuejs.org/v2/api/#vm-emit)，触发当前实例上的事件。附加参数会传给监听器回调。
- [`vm.$off([event,callback])`](https://cn.vuejs.org/v2/api/#vm-off)，移除自定义事件监听器。
  - 没参数，移除所有。
  - 只提供事件，移除该事件所有的监听器。
  - 提供事件和回调，移除指定监听器。
- [`vm.$once(event,callback)`](https://cn.vuejs.org/v2/api/#vm-once)，监听自定义事件，只触发一次，触发完成后移除监听器。

### slot插槽

> [插槽](https://cn.vuejs.org/v2/guide/components.html#%E4%BD%BF%E7%94%A8%E6%8F%92%E6%A7%BD%E5%88%86%E5%8F%91%E5%86%85%E5%AE%B9)，插槽可以看作是占位符，插槽内的内容只有**无要插入内容**时才会显示。

`my-component`组件模板：

```html
<div>
  <h2>我是子组件的标题</h2>
  <slot>
    只有在没有要分发的内容时才会显示。
  </slot>
</div>
```

父组件模板：

```html
<div>
  <h1>我是父组件的标题</h1>
  <my-component>
    <p>这是一些初始内容</p>
    <p>这是更多的初始内容</p>
  </my-component>
</div>
```

渲染结果：

```html
<div>
  <h1>我是父组件的标题</h1>
  <div>
    <h2>我是子组件的标题</h2>
    <p>这是一些初始内容</p>
    <p>这是更多的初始内容</p>
  </div>
</div>
```

还可以插入到指定位置，称为[具名插槽](https://cn.vuejs.org/v2/guide/components.html#%E5%85%B7%E5%90%8D%E6%8F%92%E6%A7%BD)：

`app-layout`组件模板：

```html
<div class="container">
  <header>
    <slot name="header"></slot>
  </header>
  <main>
    <slot></slot>
  </main>
  <footer>
    <slot name="footer"></slot>
  </footer>
</div>
```

父组件模板：

```html
<app-layout>
  <h1 slot="header">这里可能是一个页面标题</h1>

  <p>主要内容的一个段落。</p>
  <p>另一个主要段落。</p>

  <p slot="footer">这里有一些联系信息</p>
</app-layout>
```

渲染结果：

```html
<div class="container">
  <header>
    <h1>这里可能是一个页面标题</h1>
  </header>
  <main>
    <p>主要内容的一个段落。</p>
    <p>另一个主要段落。</p>
  </main>
  <footer>
    <p>这里有一些联系信息</p>
  </footer>
</div>
```

另外，[作用域插槽](https://cn.vuejs.org/v2/guide/components.html#%E4%BD%9C%E7%94%A8%E5%9F%9F%E6%8F%92%E6%A7%BD)先留着。

## 过渡动画

动画

- [进入/离开 & 列表过渡](https://cn.vuejs.org/v2/guide/transitions.html)
  - `<transition>`标签包裹，注意`name`和`mode`属性
- [状态过渡](https://cn.vuejs.org/v2/guide/transitioning-state.html)
