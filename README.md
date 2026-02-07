# SpringBootV1（Reggie 外卖示例）

> 一个基于 **Spring Boot + MyBatis-Plus + Vue2 静态页面** 的前后端同仓项目。

## 1. 项目结构总览

```text
SpringBootV1/
├── pom.xml                         # 根聚合 POM（模块管理）
├── .gitignore
├── README.md
└── reggie/                         # 主业务模块
    ├── pom.xml                     # Spring Boot 子模块 POM
    ├── db/
    │   └── db.sql                  # 额外数据库变更 SQL
    ├── upload/                     # 上传图片目录（运行时文件）
    ├── src/main/java/com/itheima/
    ├── src/main/resources/
    └── src/test/java/com/itheima/
```

---

## 2. 所有源码文件说明（Java）

> 下面按包将 `reggie/src/main/java/com/itheima` 下所有 Java 源码文件逐一列出。

### 2.1 启动与配置

- `ReggieApplication.java`：Spring Boot 启动类。
- `config/MyBatisPlusConfig.java`：MyBatis-Plus 分页拦截器配置。
- `config/WebMvcConfig.java`：静态资源映射、消息转换器扩展。

### 2.2 控制器（Controller）

- `controller/AddressBookController.java`：地址簿接口。
- `controller/CategoryController.java`：分类接口。
- `controller/CommonController.java`：通用上传/下载接口。
- `controller/DishController.java`：菜品接口。
- `controller/EmployeeController.java`：员工登录/管理接口。
- `controller/OrderController.java`：订单接口。
- `controller/OrderDetailController.java`：订单明细接口。
- `controller/SetmealController.java`：套餐接口。
- `controller/ShoppingCartController.java`：购物车接口。
- `controller/UserController.java`：C 端用户登录/短信验证码接口。

### 2.3 DTO

- `dto/DishDto.java`：菜品扩展传输对象（含口味、分类名等）。
- `dto/SetmealDto.java`：套餐扩展传输对象。

### 2.4 实体（Entity）

- `entity/AddressBook.java`
- `entity/Category.java`
- `entity/Dish.java`
- `entity/DishFlavor.java`
- `entity/Employee.java`
- `entity/OrderDetail.java`
- `entity/Orders.java`
- `entity/Setmeal.java`
- `entity/SetmealDish.java`
- `entity/ShoppingCart.java`
- `entity/User.java`

### 2.5 过滤器

- `filter/LoginCheckFilter.java`：登录校验过滤器。

### 2.6 Mapper（数据访问层）

- `mapper/AddressBookMapper.java`
- `mapper/CategoryMapper.java`
- `mapper/DishFlavorMapper.java`
- `mapper/DishMapper.java`
- `mapper/EmployeeMapper.java`
- `mapper/OrderDetailMapper.java`
- `mapper/OrderMapper.java`
- `mapper/SetmealDishMapper.java`
- `mapper/SetmealMapper.java`
- `mapper/ShoppingCartMapper.java`
- `mapper/UserMapper.java`

### 2.7 公共组件（common）

- `reggie/common/BaseContext.java`：基于 ThreadLocal 的用户上下文。
- `reggie/common/Constants.java`：全局常量。
- `reggie/common/CustomeException.java`：自定义业务异常。
- `reggie/common/GlobalExceptionHandler.java`：全局异常处理。
- `reggie/common/JacksonObjectMapper.java`：JSON 序列化处理。
- `reggie/common/MyMetaObjecthandler.java`：MyBatis-Plus 自动填充。
- `reggie/common/R.java`：统一返回对象。

### 2.8 Service 接口

- `service/AddressBookService.java`
- `service/CategoryService.java`
- `service/DishFlavorService.java`
- `service/DishService.java`
- `service/EmployeeService.java`
- `service/OrderDetailService.java`
- `service/OrderService.java`
- `service/SetmealDishService.java`
- `service/SetmealService.java`
- `service/ShoppingCartService.java`
- `service/UserService.java`

### 2.9 Service 实现

- `service/impl/AddressBookServiceImpl.java`
- `service/impl/CategoryServiceImpl.java`
- `service/impl/DishFlavorServiceImpl.java`
- `service/impl/DishServiceImpl.java`
- `service/impl/EmployeeServiceImpl.java`
- `service/impl/OrderDetailServiceImpl.java`
- `service/impl/OrderServiceImpl.java`
- `service/impl/SetmealDishServiceImpl.java`
- `service/impl/SetmealServiceImpl.java`
- `service/impl/ShoppingCartServiceImpl.java`
- `service/impl/UserServiceImpl.java`

### 2.10 工具类

- `utils/SMSUtils.java`：短信发送工具。
- `utils/ValidateCodeUtils.java`：验证码生成工具。

---

## 3. 资源文件说明（resources）

### 3.1 配置文件

- `src/main/resources/application.yml`：端口、数据源、MyBatis-Plus、上传路径等配置。

### 3.2 前端静态资源

- `src/main/resources/static/backend/`：管理端页面（Vue + ElementUI + axios）。
  - 典型入口：`backend/index.html`、`backend/page/login/login.html`
  - 包含 `api/`、`js/`、`styles/`、`images/`、`plugins/` 等目录。
- `src/main/resources/static/front/`：用户端页面（Vue + Vant）。
  - 典型入口：`front/index.html`
  - 包含 `page/`、`js/`、`styles/`、`images/`、`fonts/` 等目录。

### 3.3 运行时文件

- `reggie/upload/`：项目上传文件保存目录（样例图片已存在）。

---

## 4. 测试文件说明

`src/test/java/com/itheima/` 中包含以下测试类：

- `TestCategory.java`
- `TestCode.java`
- `TestDish.java`
- `TestEmployee.java`
- `TestSetmeal.java`

主要用于本地联调与基础功能验证。

---

## 5. 数据库准备流程

> 该项目使用 MySQL。请先准备名为 `reggie` 的数据库。

1. 先执行完整建表脚本：

```sql
source /workspace/SpringBootV1/reggie/db/schema.sql;
```

2. 再执行补丁脚本：

```sql
source /workspace/SpringBootV1/reggie/db/db.sql;
```

> 说明：
> - `schema.sql` 是完整建表脚本（包含本项目所需核心表）。
> - `db.sql` 是补丁脚本（用于补充 `employee.type` 字段与数据修正）。
> - 如果你看到 `ERROR 1146 (42S02): Table 'reggie.employee' doesn't exist`，通常是第 1 步没有成功执行（例如 source 路径错误或未切换到正确文件路径）。

---

## 6. 本地运行流程（推荐）

## 6.1 环境要求

- JDK 17（项目属性指定 17）
- Maven 3.8+
- MySQL 8.x

## 6.2 修改配置

编辑：`reggie/src/main/resources/application.yml`

重点确认：

- `spring.datasource.druid.url`
- `spring.datasource.druid.username`
- `spring.datasource.druid.password`
- `reggie.path`（上传文件目录）

建议把 `reggie.path` 改成本机存在的目录，例如：

```yaml
reggie:
  path: /tmp/reggie/upload
```

并提前创建目录：

```bash
mkdir -p /tmp/reggie/upload
```

## 6.3 启动项目

在仓库根目录执行：

```bash
mvn -pl reggie spring-boot:run
```

如果你希望先打包再运行：

```bash
mvn -pl reggie clean package -DskipTests
java -jar reggie/target/reggie-*.jar
```

## 6.4 访问地址

默认端口 `8080`：

- 管理端：`http://localhost:8080/backend/index.html`
- 用户端：`http://localhost:8080/front/page/login.html`


---

## 7. 常见问题排查

1. **启动报数据库连接失败**
   - 检查 MySQL 是否启动。
   - 检查 `application.yml` 中 URL/账号/密码。

2. **图片上传后不显示**
   - 检查 `reggie.path` 是否存在且可写。
   - 检查 `upload` 接口是否返回成功。

3. **构建依赖下载失败（403/网络问题）**
   - 检查 Maven 仓库网络访问。
   - 配置可用镜像源后重试。

---

## 8. 一键查看“全部文件”的命令

如果你想在本地再次生成完整文件清单，可执行：

```bash
find . -type f | sort
```

按目录过滤查看：

```bash
find reggie/src/main/java -type f | sort
find reggie/src/main/resources/static -type f | sort
find reggie/src/test/java -type f | sort
```

