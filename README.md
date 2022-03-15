# SophistNerd
Practice Android Fundamental 

Practice make perfect



## gradle 7中项目参考源位置调整
- 仓库源配置位置修改
之前，项目依赖除了在项目根目录的build.gradle文件中配置仓库源依赖；
现在，项目的仓库源配置到了settings.gradle文件中！
  
- ‘Gradle Libs’ was added by unknown code
  
```text
问题：Build was configured to prefer settings repositories over project repositories but repository ‘Gradle Libs’ was added by unknown code

解决方法：settings.gradle中，去除repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
```