Nginx 配置反向代理，将所有 /node-api 请求通过代理 127.0.0.1:7890 转发到 https://developers.google.com/：

确保你已经安装并运行 Nginx。
打开 Nginx 配置文件（通常位于 /etc/nginx/nginx.conf 或 /etc/nginx/sites-available/default）。
添加以下配置：

该配置未验证，提供灵感
```nginx configuration
http {
    include       mime.types;
    default_type  application/octet-stream;

    server {
        listen 80;
        server_name your_domain.com;

        location /node-api/ {
            proxy_pass https://developers.google.com/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;

            # 配置代理服务器
            proxy_http_version 1.1;
            proxy_set_header Connection "";
            proxy_buffering off;

            # 配置本地代理
            resolver 127.0.0.1;
            proxy_set_header Proxy-Authorization "Basic $(echo -n 'aaa:aaaca' | base64)";
            proxy_pass http://127.0.0.1:2222;
        }
    }
}

```
