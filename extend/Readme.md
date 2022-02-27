# 为验证Activity-Fragment结构下的生命周期变化

相对布局引用文件 : Activity-Fragment-Lifecycle.png

当然可以写(Linux中./，Windows中.\)，也可以直接省去相对路径。

![Activity-Fragment-Lifecycle](.\Activity-Fragment-Lifecycle.png)

## HomeFragment启动
### HomeFragment先于宿主Activity构建视图的
- HostActivity通知HomeFragment需要被加载
- HomeFragment构建视图 onAttach->onCreate->onCreateView->onViewCreated

### HostActivity构建
- HostActivity构建 onCreate
- HomeFragment收到通知 onActivityCreate->onStart
- HostActivity进入前台 onStart->onResume
- HomeFragment进入前台 onStart->onResume
- HostActivity添加到窗口 onAttachedToWindow

### HostActivity和HomeFragment进入后台菜单(注意Activity当前展示的Fragment不会销毁视图)
- HomeFragment进入后台 onPause->onStop
- HostFragment进入后台 onPause->onStop

### HostActivity和HomeFragment从后台菜单恢复(注意Activity当前展示的Fragment不会销毁视图)
- HomeFragment onStart-onResume
- HostActivity onStart-onResume


### HomeFragment跳转Non-HomeFragment（常规Fragment跳转逻辑）
- HomeFragment准备进入后台 onPause->onStop
- Non-Fragment准备进入前台 onAttach->onCreate->onCreateView->onViewCreated->onStart->onResume
- HomeFragment准备销毁视图 onDestroyView


### Non-HomeFragment启动Non-HostActivity（常规Activity跳转逻辑）
- Non-HomeFragment准备进入后台 onPause
- HostActivity准备进入后台 onPause
- Non-HostActivity准备进入前台 onCreate->onStart->onResume->onAttachToWindow
- Non-HomeFragment销毁视图 onStop->onDestroyView
- HostActivity完全进入后台 onStop





