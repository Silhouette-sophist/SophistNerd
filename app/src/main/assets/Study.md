
详细教程：https://www.yiibai.com/git/git_reset.html

# git配置

## 查看配置
```text
//查看所有配置
git config -l
//查看系统配置
git config --system --list
//查看用户自己配置
git config --global --list
```

文件位置：/program/Git/etc/gitconfig

## 配置数据
```text
//配置用户名
git config --global user.name  "obgeName"
//配置邮箱
git config --global user.email "824307555@qq.com"
```
文件位置：Users/Administrator/.gitconfig

# 配置ssh key
Git是分布式的代码管理工具，远程的代码管理是基于SSH的，所以要使用远程的Git则需要SSH的配置。

- 查看是否已经有了ssh密钥：cd ~/.ssh
如果没有密钥则不会有此文件夹，有则备份删除
- 生存密钥：
```text
ssh-keygen -t rsa -C “C_jin928@163.com”
```

```text
Your identification has been saved in C:\Users\Administrator/.ssh/id_rsa.
Your public key has been saved in C:\Users\Administrator/.ssh/id_rsa.pub.
```

- 添加密钥到ssh：ssh-add 文件名
- 在github上添加ssh密钥，这要添加的是“id_rsa.pub”里面的公钥
- 测试：ssh git@github.com

# 仓库初始化
## git init
```text
…or create a new repository on the command line
echo "# Annotations" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin git@github.com:Silhouette-sophist/Annotations.git
git push -u origin main

…or push an existing repository from the command line
git remote add origin git@github.com:Silhouette-sophist/Annotations.git
git branch -M main
git push -u origin main
```
## git clone

# 分支绑定
```text
git remote add //绑定一个远端仓库

git branch --set-upstream-to=origin/远端分支名 本地分支名
```

# 同步分支
## git fetch

## git pull

# 提交代码
## git add .
## git commit
## git status 
显示工作目录和暂存区的状态
## git push origin 本地分支 origin/远程分支名

# .gitignore文件
.idea、build文件夹中的内容没必要提交要仓库，可以增加忽略

# 暂存
## git stash save “”
## git stash pop
## git stash list
## git stash drop stash@{0}

# 分支管理
# git checkout -b 本地分支名 [远端分支名]
# git branch 本地分支名，或者git checkout 本地分支名
# git branch -d 本地分支名

# merge分支
https://www.yiibai.com/git/git_merge.html
# git merge 分支A 分支B

# git diff

# 撤销本地提交
## git reset --soft HEAD^
## 撤回文件提交，等等很多看教程吧

