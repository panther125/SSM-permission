// 导入用来创建路由和确定路由模式的两个方法
import store from '@/store';
import storage from '@/util/storage'
import {
    createRouter,
    createWebHistory
} from 'vue-router'
/**
 * 定义路由信息
 * 
 */
const routes = [{
    name: 'login',
    path: '/login',
    component: () =>
        import ('@/components/login'),
}, {
    name: 'main',
    path: '/main',
    component: () =>
        import ('@/components/main'),
    children: [{
            name: 'user',
            path: '/user',
            component: () =>
                import ('@/components/system/user')
        }

    ]
}]

// 创建路由实例并传递 `routes` 配置
// 我们在这里使用 html5 的路由模式，url中不带有#，部署项目的时候需要注意。
const router = createRouter({
    history: createWebHistory(),
    routes,
})


// 全局的路由守卫 
router.beforeEach((to) => {

    // 1. 如果前往登录页面放行
    if (to.name === "login") {
        return true;
    }
    // 2. 否则检查用户是否等录
    if (!store.getters.isLogin) {
        // 1. 判断storage是否有用户信息
        if (!storage.getSessionObject("username")) {
            router.push({ name: 'login' });
        } else {
            store.dispatch("RECOVERY_USER");
            store.dispatch("GET_INFO");
        }
    }

    // 3. 没有登录跳转到登录界面

    return true
})

// 讲路由实例导出
export default router