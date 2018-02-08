> 通过一些接触，Vue简单说就是管理指定界面(*`el`*)对应的数据(*`data`*)和行为(*`methods`*)，是一个典型的`ViewModel`实现。

## 内部指令

1. `v-if`
    ```html
    <p v-if="!shouldShow">{{msg}}</p>
    ```
1. `v-show`
    ```html
    <p v-show="5>3">{{msg}}</p>
    ```
    > 注意`v-if`和`v-show`的差别，`v-if='false'`的情况下，元素不会被包含在`html`文档中，相比之下`v-show='false'`只是设置了`style="display: none;"`
1. `v-for`
    ```html
    <p v-for="item in items">{{item.content}}</p>

    <!--如果需要使用索引-->
    <p v-for="(item,index) in items">{{item.content}}</p>
    ```
    ```js
    //在data属性内
    items: [
      { content: '第一个元素' },
      { content: '第二个元素' },
      { content: '第三个元素' },
    ]
    ```
    ```html
    <!--生成结果-->
    <p>第一个元素</p>
    <p>第二个元素</p>
    <p>第三个元素</p>
    ```
    > `v-for`可以理解成，把我们写的html作为一份模板，以数组的**每个子元素**作为**数据**生成多个HTML元素。
1. 计算属性
    ```html
    <!--修改一下数据源-->
    <p v-for="item in sortedItems">{{item}}</p>
    ```
    ```js
    //修改数据源并实现倒叙排列数据
    new Vue({
        el: '#app',
        data: {
            items: [
                1,5,2,6,3,4,7,10,9
            ]
        },
        computed: {
            sortedItems() {
                return this.items.sort((a, b) => b - a)
            }
        }
    })
    ```
1. `v-text`和`v-html`，如果按照上边的方式直接使用`{{}}`，则在对应的script没有被执行到的时候，会先显示`{{balabala}}`。这种情况可以通过指定属性解决，就是`v-text`和`v-html`解决。(*还有一种解决方式`v-cloak`，后续会提到*)
    ```html
    <!--使用v-text和使用{{}}效果一致，都是将内容解析为纯文本-->
    <p v-for="item in items" v-text="item"></p>

    <!--如果需要解析HTML文本，就不能使用{{}}，此时需要使用v-html-->
    <p v-for="item in items" v-html="item"></p>
    ```
1. `v-model`，数据绑定。界面对数据的改动，会同步修改js中的属性值。适用于各种`input`。
    > 使用上和`v-text`使用一样，可以理解为`v-model`是`v-text`加上了一个**修改事件监听**，在监听到改动的时候，会去修改对应的`data`中的值。

    修饰符：
    - `v-model.lazy`，失去焦点后改变。
    - `v-model.number`，先输入数字，之后输入字符串则不计算。如果先输入字符串的话，就没卵用。
    - `v-model.trim`，前后空格不计算。
1. `v-on`事件监听，简写方式: `@`
    ```html
    <input type="text" v-model="count" @keyup.enter="onEnter">
    </input>
    <button @click="goUp">Up</button>
    <button @click="goDown">Down</button>
    ```
    ```js
    data: {
        count: 0
    },

    //注意这个方法，event是原生的DOM事件
    onEnter(event) {
      event.target.blur();
      setTimeout(() => {
        alert(this.count);
      }, 0);
    },
    goUp() {
      this.count = Number(this.count) + 1;
    },
    goDown() {
      this.count = Number(this.count) + 1;
    }
    ```
1. `v-bind`，绑定标签上的属性。简写方式： `:`
    ```html
    <img :src="vueLogo">
    ```
    ```js
    data: {
        vueLogo: require("./assets/logo.png"),
        //之后需要用到的
        isClassA: false,
        classNameA: 'classA',
        classNameB: 'classB',
        colorName: 'blue',
        styleObj: {
            color: 'red',
            fontFamily: 'Century Gothic'
        }
    },
    ```

    ```html
    <!--  类绑定  -->
    <!-- 1. 动态绑定一个class -->
    <p :class="classNameA"></p>

    <!-- 2. 根据boolean决定是否添加class，注意{} -->
    <p :class="{classA：isClassA}"></p>

    <!-- 3. 根据boolean切换class -->
    <p :class="isClassA ? classNameA : classNameB"></p>

    <!-- 4. 类数组 -->
    <p :class="[classNameA,classNameB]">


    <!--  样式绑定  -->
    <!-- 1. 样式值 -->
    <p :style="{color:colorName}"></p>

    <!-- 2. 样式对象 -->
    <p :style="styleObj"></p>
    ```
1. 其他指令
    - `v-pre`，添加这个之后，会显示原始值。即`<p v-pre>{{msg}}</p>`会直接显示`{{msg}}`
    - `v-cloak`，指定渲染完整个DOM后才进行显示。需要写在css样式里`[v-cloak] { display: none; }`，参考[TodoMVC](https://jsfiddle.net/yyx990803/4dr2fLb7/)
    - `v-once`，数据后续更改不再渲染。

---

## 全局API

### 自定义指令

> [自定义指令](https://cn.vuejs.org/v2/guide/custom-directive.html)，文档说的很好。

需要注意的地方：

1. 多个[钩子函数](https://cn.vuejs.org/v2/guide/custom-directive.html#%E9%92%A9%E5%AD%90%E5%87%BD%E6%95%B0)对应不同的生命周期。
1. 如果只需要`bind`和`update`，可以直接将指令构造成一个函数，[函数简写](https://cn.vuejs.org/v2/guide/custom-directive.html#%E5%87%BD%E6%95%B0%E7%AE%80%E5%86%99)。
1. 钩子函数传递的[参数](https://cn.vuejs.org/v2/guide/custom-directive.html#%E9%92%A9%E5%AD%90%E5%87%BD%E6%95%B0%E5%8F%82%E6%95%B0)有四个：
    1. DOM节点`el`
    1. 绑定信息`binding`
    1. Vue虚拟节点`vnode`
    1. 之前的虚拟节点`oldVnode`
1. 指令函数可以接受所有合法的JavaScript表达式，所以，binding的值可以是一个[对象](https://cn.vuejs.org/v2/guide/custom-directive.html#%E5%AF%B9%E8%B1%A1%E5%AD%97%E9%9D%A2%E9%87%8F)

### Vue.extend 扩展

> 扩展实例构造器，预设了部分选项。所以可以直接使用new extendName()快速获取一个Vue实例，也可以基于该实例而不是`Vue`来进行扩展。

```html
<div id="author"></div>

<author></author>
```

```js

var authorExtend = Vue.extend({
  template: "<p><a :href='authorURL'>{{authorName}}</a></p>",
  data() {
    return {
      authorName: 'GL',
      authorURL: 'https://www.baidu.com'
    }
  }
});

// id的方式，会将div的内部替换
new authorExtend().$mount("#author");

// 标签的方式，会直接将该标签替换掉
new authorExtend().$mount("author");
```

### Vue.set()

Vue的data可以是外部的数据对象，也可以通过三种方式去修改数据。

```js
var outData = {
  count: 0
}

var app = new Vue({
  el: '#app',
  data: outData,
  methods: {
    add() {
        //直接操作外部对象
        outData.count++;

        //通过Vue实例操作对象，此处也可以是this
        app.count++;

        //通过Vue.set()操作对象。
        Vue.set(outData, 'count', this.count + 1);
    }
  },
})
```

但是因为JavaScript的限制：

- 当你**利用索引直接设置**一个项时，vue不会为我们自动更新。
- 当你**修改数组的长度**时，vue不会为我们自动更新。

这时可以通过下面的方式通知数据改变：

```js
var outData = {
  array: [1, 2, 3, 4, 5]
}

var app = new Vue({
  el: '#app',
  data: outData,
  methods: {
    add() {
      Vue.set(outData.array, 0, 100);
    }
  },
})
```

### Vue的生命周期

> [Vue生命周期](https://cn.vuejs.org/v2/guide/instance.html#%E5%AE%9E%E4%BE%8B%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F%E9%92%A9%E5%AD%90)

没什么可说的。对生命周期这玩意太熟悉了。使用如下：

```js
new Vue({
  data: {
    a: 1
  },
  created: function () {
    // `this` 指向 vm 实例
    console.log('a is: ' + this.a)
  }
})
```

### Vue模板

> 直接将与Vue关联的HTML元素（*比如div*）替换为模板内容。

1. 在构造器里写
    ```js
    var app = new Vue({
        el: '#app',
        data: {
            msg: 'Hello Vue!',
        },
        template: `
            <h1 style="color:red">{{msg}}</h1>
        `
    })
    ```
1. 在HTML里写。
    ```html
    <div id="app"></div>
    <template id="template">
        <h1 style="color:red">{{msg}}</h1>
    </template>
    ```
    ```js
    var app = new Vue({
        el: '#app',
        data: {
            msg: 'Hello Vue!',
        },
        template: '#template'
    })
    ```
1. 用`<script>`标签，注意`type`属性为Vue特定的`x-template`，使用和上边的`HTML`方式很相似。好处是之后可以写成外部文件，通过`src`引入。
    ```html
    <div id="app"></div>
    <script type="x-template" id='template'>
        <h1 style="color:red">{{ msg }}</h1>
    </script>
    ```

### 组件 Component

> [Vue组件](https://cn.vuejs.org/v2/guide/components.html)

#### 组件的注册

1. 全局注册，可用于所有的Vue实例
    ```html
    <div id="example">
        <my-component></my-component>
    </div>
    ```
    请在初始化之前注册组件：
    ```js
    // 注册
    Vue.component('my-component', {
        template: '<div>A custom component!</div>'
    })

    // 创建根实例
    new Vue({
        el: '#example'
    })
    ```
    渲染结果：
    ```html
    <div id="example">
        <div>A custom component!</div>
    </div>
    ```
1. 局部注册，只可用于当前Vue实例
    ```js
    var Child = {
        template: '<div>A custom component!</div>'
    }

    new Vue({
        // ...
        components: {
            // <my-component> 将只在父组件模板中可用
            'my-component': Child
        }
    })
    ```

#### 自定义组件属性

1. 通过`props`实现，和`template`同级的一个属性。注意带有`-`的写法。
    ```js
    Vue.component('child', {
    // 声明 props
    props: ['myMessage'],
    // 就像 data 一样，prop 也可以在模板中使用
    // 同样也可以在 vm 实例中通过 this.message 来使用
    template: '<span>{{ myMessage }}</span>'
    })
    ```

    ```html
    <child my-message="hello!"></child>
    ```

1. 传递一个对象的所有属性：
    ```js
    todo: {
    text: 'Learn Vue',
    isComplete: false
    }
    ```
    然后：
    ```html
    <todo-item v-bind="todo"></todo-item>
    ```
    等价于：
    ```html
    <todo-item
        v-bind:text="todo.text"
        v-bind:is-complete="todo.isComplete"
    ></todo-item>
    ```

#### 组件中引用自定义组件-`父子组件`

和`template`同级的还有一个`components`属性。先注册好组件，然后直接引用即可。

```js
var child = {
    template: 'balabala'
}

var father = {
    template: '<child></child>',
    components: {
        'child': child
    }
}
```

#### 使用`<component>`动态绑定组件

```js
var vm = new Vue({
  el: '#example',
  data: {
    currentView: 'home'
  },
  components: {
    home: { /* ... */ },
    posts: { /* ... */ },
    archive: { /* ... */ }
  }
})
```

```html
<component v-bind:is="currentView">
  <!-- 组件在 vm.currentview 变化时改变！ -->
</component>
```

实例，点击切换模板：

```html
<div id="app">
    <component :is="currentComponent"></component>
    <button @click="change">Change</button>
</div>
```

```js
var component1 = {
  template: `<h1 style='color:red;'>组件1</h1>`
}
var component2 = {
  template: `<h1 style='color:blue;'>组件2</h1>`
}

var app = new Vue({
  el: '#app',
  data: {
    currentComponent: 'Component1'
  },
  components: {
    Component1: component1,
    Component2: component2
  },
  methods: {
    change() {
      this.currentComponent = this.currentComponent == 'Component1' ? 'Component2' : 'Component1';
    }
  }
})
```
