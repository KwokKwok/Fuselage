# Vuex学习记录

## Vue中通过全局Vue来通信（*Bus*）

先写一个`bus.js`，只创建一个Vue实例并导出：

```js
import Vue from 'vue';

export default new Vue;
```

然后写两个组件，里面都只有一个`count`字段，点击自己的时候，让对方的`count`加上自己的`count`值，并显示在界面上：

两个组件，导入`bus.js`中的实例：

```html
<template>
  <div>
      <h2 style="color:red;" @click="leftClick">{{count}}</h2>
  </div>
</template>

<script>
import bus from "@/bus.js";
export default {
  data() {
    return {
      count: 1
    };
  },
  methods: {
    leftClick() {
      bus.$emit("leftClick", this.count);
    }
  },
  created() {
    bus.$on("rightClick", count => {
      this.count += count;
    });
  }
};
</script>
```

```html
<template>
  <div>
      <h2 style="color:blue;" @click="rightClick">{{count}}</h2>
  </div>
</template>

<script>
import bus from "@/bus.js";
export default {
  data() {
    return {
      count: 1
    };
  },
  methods: {
    rightClick() {
      bus.$emit("rightClick", this.count);
    }
  },
  created() {
    bus.$on("leftClick", count => {
      this.count += count;
    });
  }
};
</script>

```

`App.vue`写两个`<router-view>`:

```html
<template>
  <div id="app">
    <router-view name="left" style="float:left;"/>
    <router-view name="right" style="float:right;"/>
  </div>
</template>
```

`router/index.js`定义好：

```js
import Left from '@/components/Left'
import Right from '@/components/Right'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      components: {
        left: Left,
        right: Right
      }
    }
  ]
})
```

这就可以了。

---

## Vuex

Vuex是一个状态管理模式，可以用来集中**存储/管理** Vue应用的所有组件 的状态。

需要先使用命令安装：

```shell
npm install vuex --save
// yarn add vuex
```

下面把`state`、`getter`、`mutation`、`action`都用了，`module`，就先不用了，需要可以再看[文档](https://vuex.vuejs.org/zh-cn/modules.html)，就是可以将`store`的内容细化成更小的模块。

首先注意两点：

1. 模块方式需要调用`Vue.use(Vuex)`
1. 操作`state`中的状态需要通过`mutation`，不能直接修改。
1. `mutation`是同步的，异步需要使用`action`
1. `getter`类似计算属性，通过`store.getters.<prop>`访问
1. `...Map`对象展开运算符，可以通过**对象**或**数组**的方式使用，区别在于是否需要直接使用`store`中的名称。*需要导入。*

在`store/index.js`中组装一个`store`导出：

```js
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const state = {
    left: 1,
    right_value: 1
}

const mutations = {
    addLeft(state, n) {
        state.left += n;
    },
    addRight(state, n) {
        state.right_value += n;
    }
}

//类似计算属性，接受state作为第一个参数
const getters = {
    right: state => state.right_value,

    // 也可以返回函数
    // getTodoById: (state) => (id) => {
    //     return state.todos.find(todo => todo.id === id)
    // },

    // 之后可以进行函数调用
    // store.getters.getTodoById(2) // -> { id: 2, text: '...', done: false }
}


const actions = {
    addLeftAsync(context, n) {
        setTimeout(() => context.commit('addLeft', n), 500)
    },
    // addLeftAsync({commit}, n) {
    //     setTimeout(() => commit('addLeft', n), 500)
    // }
}

export default new Vuex.Store({
    state, mutations, actions, getters
})
```

在`main.js`导入到根组件中：

> 通过在根实例中注册 store 选项，该 store 实例会注入到根组件下的所有子组件中，且子组件能通过 this.$store 访问到。

```js
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'

Vue.config.productionTip = false

/* eslint-disable no-new */

var vue = new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
```

在子组件中访问：

1. `Left.vue`
    ```html
    <template>
        <div>
            <h2 style="color:red;" @click="leftClick">{{count}}</h2>
        </div>
    </template>

    <script>
    import { mapState, mapMutations } from "vuex";
    export default {
        computed: {
            // 自身的计算属性可以放在localComputed中
            localComputed: {},
            ...mapState({
                count: "left"
                // 相当于 count: state=>state.left
            })
        },
        methods: {
            leftClick() {
                this.addRight(this.count);
                // 相当于 this.$store.commit('addRight',this.count);
            },
            // 数组方式的map
            ...mapMutations(["addRight"])
        }
    };
    </script>
    ```
1. `Right.vue`
    ```html
    <template>
        <div>
            <h2 style="color:blue;" @click="rightClick">{{count}}</h2>
        </div>
    </template>

    <script>
    import { mapState, mapMutations, mapActions, mapGetters } from "vuex";
    export default {
        computed: {
            ...mapGetters({
                count: "right"
            })
        },
        methods: {
            ...mapActions({
                add: "addLeftAsync"
            }),
            rightClick() {
                this.add(this.count);
                // 相当于 this.$store.dispatch('addLeftAsync',this.count);
            }
        }
    };
    </script>
    ```
