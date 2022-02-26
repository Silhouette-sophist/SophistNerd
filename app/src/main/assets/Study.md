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
# git init

