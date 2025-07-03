
# Excel数据分析系统 - Spring Boot后端

这是一个基于Spring Boot的Excel数据分析系统，支持上传Excel文件、数据解析和统计分析。

## 功能特性

- Excel文件上传和解析（支持.xls和.xlsx格式）
- 数据统计分析（数值类型和文本类型识别）
- RESTful API接口
- 跨域支持
- 文件大小限制配置

## 技术栈

- Java 17
- Spring Boot 3.2.0
- Apache POI 5.2.4
- Maven

## 快速开始

### 环境要求

- JDK 17或更高版本
- Maven 3.6+

### 运行步骤

1. 克隆项目
```bash
git clone <repository-url>
cd excel-analyzer
```

2. 编译和运行
```bash
mvn clean install
mvn spring-boot:run
```

3. 服务启动后访问: http://localhost:8080

## API接口

### 1. 上传Excel文件

**POST** `/api/upload`

**请求参数：**
- `file`: MultipartFile - Excel文件

**响应示例：**
```json
{
    "data": [
        {
            "列名1": "值1",
            "列名2": 123
        }
    ],
    "headers": ["列名1", "列名2"],
    "fileName": "test.xlsx",
    "totalRows": 100,
    "totalColumns": 5,
    "statistics": {
        "列名1": {
            "type": "text",
            "count": 100,
            "uniqueValues": 50
        },
        "列名2": {
            "type": "numeric",
            "count": 100,
            "average": 123.45,
            "max": 500,
            "min": 10
        }
    }
}
```

### 2. 生成图表数据

**POST** `/api/generate-chart`

**请求体：**
```json
{
    "chartType": "bar",
    "xAxis": "列名1",
    "yAxis": "列名2"
}
```

## 配置说明

### 文件上传限制
在 `application.properties` 中配置：
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 端口配置
```properties
server.port=8080
```

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/example/excelanalyzer/
│   │       ├── ExcelAnalyzerApplication.java
│   │       ├── controller/
│   │       │   └── FileUploadController.java
│   │       ├── model/
│   │       │   ├── DataAnalysisResult.java
│   │       │   └── ColumnStatistics.java
│   │       ├── service/
│   │       │   └── ExcelAnalysisService.java
│   │       └── config/
│   │           └── WebConfig.java
│   └── resources/
│       └── application.properties
└── pom.xml
```

## 开发说明

### 添加新功能
1. 在 `service` 包中添加业务逻辑
2. 在 `controller` 包中添加API接口
3. 在 `model` 包中添加数据模型

### 数据库集成
如需持久化数据，可添加以下依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## 部署

### 打包
```bash
mvn clean package
```

### 运行JAR文件
```bash
java -jar target/excel-analyzer-1.0.0.jar
```

## 许可证

MIT License
