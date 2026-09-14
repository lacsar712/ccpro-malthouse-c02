# MaltHouse · 麦汁发酵罐台账

精酿厂发酵过程记录演示项目：管理酿造厂区、发酵罐、发酵批次与比重读数，并提供运行仪表盘。

## 技术栈

- **后端**: Java 17 + Spring Boot 3.2 + Spring Data JPA + Spring Security (JWT HS256) + Maven
- **前端**: Vue 3 + Vite + Pinia + Vue Router（Composition API + `<script setup>`）
- **数据库**: MySQL 8.0
- **部署**: Docker Compose；前端 Nginx 反代 `/api` 到后端

## 一键启动

```bash
docker compose up --build
```

启动完成后访问：

| 服务 | 地址 |
|------|------|
| 前端 | http://localhost:3300 |
| 后端 API | http://localhost:8300/api |
| MySQL | localhost:3308（用户 `root` / 密码 `root`，库名 `malthouse`） |

停止服务：

```bash
docker compose down
```

清除数据卷后重建：

```bash
docker compose down -v
docker compose up --build
```

## 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| `admin` | `123456` | 管理员 |
| `brewer` | `123456` | 酿造员 |

启动时由 `DataSeeder`（ApplicationRunner）自动建表示例数据；密码使用 BCrypt 存储。

## 功能模块

1. **Auth** — JWT 登录
2. **BrewerySite** — 酿造厂区（name / location / notes）
3. **Fermenter** — 发酵罐（siteId / tankCode / capacityLiters / status: idle\|fermenting\|cip）
4. **BrewBatch** — 发酵批次（fermenterId / recipeName / brewDate / originalGravity / targetFg / status: planned\|active\|packaged\|dumped）
5. **GravityReading** — 比重读数（batchId / measuredAt / specificGravity / temperatureC / notes）
6. **Dashboard** — 罐总数、active 批次数、近 7 日读数次数、按 status 批次计数

### 业务约束

- **罐号唯一性**：`tankCode` 在**同一厂区（site）内唯一**（数据库唯一约束 `uk_fermenter_site_tank` + 服务端校验）。

## API 前缀

所有接口前缀：`/api`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 登录（公开） |
| GET | `/api/health` | 健康检查（公开） |
| GET/POST | `/api/sites` | 厂区列表 / 创建 |
| GET/PUT/DELETE | `/api/sites/{id}` | 厂区详情 / 更新 / 删除 |
| GET/POST | `/api/fermenters` | 发酵罐列表 / 创建 |
| GET/PUT/DELETE | `/api/fermenters/{id}` | 发酵罐详情 / 更新 / 删除 |
| GET/POST | `/api/batches` | 批次列表 / 创建 |
| GET/PUT/DELETE | `/api/batches/{id}` | 批次详情 / 更新 / 删除 |
| GET/POST | `/api/readings` | 读数列表 / 创建（可选 `?batchId=`） |
| GET/PUT/DELETE | `/api/readings/{id}` | 读数详情 / 更新 / 删除 |
| GET | `/api/dashboard` | 仪表盘汇总 |

除登录与健康检查外，请求头需携带：`Authorization: Bearer <token>`。错误响应为 JSON：`{"message":"..."}`。

## 目录结构

```
MaltHouse-01/
├── docker-compose.yml
├── README.md
├── .gitignore
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/malthouse/
│       ├── MaltHouseApplication.java
│       ├── config/          # Security / CORS
│       ├── controller/      # REST API
│       ├── dto/
│       ├── entity/
│       ├── exception/
│       ├── repository/
│       ├── security/        # JWT
│       ├── seed/            # 启动种子数据
│       └── service/
└── frontend/
    ├── Dockerfile
    ├── nginx.conf
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── api/
        ├── assets/
        ├── components/
        ├── router/
        ├── stores/
        └── views/
```

## 本地开发（可选）

后端（需本机 MySQL 3308 或改 `application.yml`）：

```bash
cd backend
mvn spring-boot:run
```

前端：

```bash
cd frontend
npm install
npm run dev
```

开发时 Vite 已将 `/api` 代理到 `http://localhost:8300`。
