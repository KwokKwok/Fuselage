生成，默认在`~/.ssh/`目录下

```bash
ssh-keygen
```

发送到服务器，免密登录

```bash
ssh-copy-id user@host
```

服务器端启用/禁用使用密码登录：

```bash
# 登录服务器后在服务器上操作
vim /etc/ssh/sshd_config
# 修改PasswordAuthentication yes/no

service sshd restart
```

使用别名代替IP

```bash
cd ~/.ssh
vim config
```

比如我需要为两个服务器设置别名，则`~/.ssh/config`内容可以是这样：

```conf
Host ali #别名
HostName 123.123.123.123 #主机IP
User root #用户名
IdentitiesOnly yes #只使用这里的ssh key，设置这个即可，不用设置下边的
IdentityFile ~/.ssh/id_rsa #设置ssh key的路径，有些人需要设置这个

Host tencent #别名
HostName 123.123.123.123 #主机IP
User root #用户名
IdentitiesOnly yes
IdentityFile ~/.ssh/id_rsa
```

登录或发送文件：

```bash
ssh $你的别名
scp file ali:/opt
```

如果提示：`Bad owner or permissions on /root/.ssh/config`

输入：(*详细解释参见[ServerFault相关问题讨论](https://serverfault.com/questions/253313/ssh-returns-bad-owner-or-permissions-on-ssh-config)*)

```bash
`chmod 600 ~/.ssh/config`
```
