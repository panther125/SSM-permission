import request from "@/api";

// 新增用户
export function login(data) {
    return request({
        url: '/login',
        method: 'post',
        data: data
    })
}

// 用户登出
export function logout() {
    return request({
        url: '/logout',
        method: 'get',
    })
}

// 分页
export function listuser(data) {
    return request({
        url: '/ydlUser',
        method: 'get',
        params: data
    })
}
// 添加用户角色和权限的接口
export function getInfo() {
    return request({
        url: '/ydlUser/getInfo',
        method: 'get',
    })
}

export function getById(id) {
    return request({
        url: '/ydlUser/' + id,
        method: 'get',
    })
}

export function add(data) {
    return request({
        url: '/ydlUser',
        method: 'post',
        data: data
    })
}