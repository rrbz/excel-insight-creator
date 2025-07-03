
# 高级Excel数据分析系统 - Spring Boot后端

这是一个功能强大的基于Spring Boot的Excel数据分析系统，支持上传Excel文件、高级数据分析、统计建模、机器学习和图表生成。

## 功能特性

### 核心功能
- **Excel文件处理**: 支持.xls和.xlsx格式文件上传和解析
- **数据持久化**: 使用JPA和H2数据库存储数据集和分析结果
- **高级统计分析**: 包括描述性统计、偏度、峰度、四分位数等
- **相关性分析**: 支持Pearson、Spearman相关性分析
- **回归分析**: 线性回归、多元回归分析
- **数据质量评估**: 异常值检测、数据完整性分析
- **机器学习**: 基于Weka的数据挖掘和预测分析
- **图表生成**: 使用JFreeChart生成多种类型的图表

### 高级分析功能
- **统计建模**: 完整的统计分析工具集
- **数据可视化**: 柱状图、折线图、散点图、饼图、热力图等
- **预测分析**: 基于历史数据的趋势预测
- **数据挖掘**: 聚类分析、关联规则挖掘
- **报告生成**: 自动生成分析报告和解释

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **数据库**: H2 Database (内存数据库)
- **ORM**: Spring Data JPA
- **Excel处理**: Apache POI 5.2.4
- **统计分析**: Apache Commons Math 3.6.1
- **图表生成**: JFreeChart 1.5.3
- **机器学习**: Weka 3.8.6
- **缓存**: Caffeine Cache
- **安全**: Spring Security
- **构建工具**: Maven
- **Java版本**: Java 17

## 项目结构

```
src/
├── main/
│   ├── java/com/example/excelanalyzer/
│   │   ├── ExcelAnalyzerApplication.java          # 主应用类
│   │   ├── config/
│   │   │   ├── SecurityConfig.java                # 安全配置
│   │   │   └── WebConfig.java                     # Web配置
│   │   ├── controller/
│   │   │   ├── FileUploadController.java          # 文件上传控制器
│   │   │   └── AdvancedAnalysisController.java    # 高级分析控制器
│   │   ├── entity/
│   │   │   ├── DataSet.java                       # 数据集实体
│   │   │   └── AnalysisResult.java                # 分析结果实体
│   │   ├── model/
│   │   │   ├── DataAnalysisResult.java            # 数据分析结果模型
│   │   │   ├── AdvancedColumnStatistics.java     # 高级列统计模型
│   │   │   ├── CorrelationAnalysis.java           # 相关性分析模型
│   │   │   ├── RegressionAnalysis.java            # 回归分析模型
│   │   │   ├── ChartRequest.java                  # 图表请求模型
│   │   │   └── ChartData.java                     # 图表数据模型
│   │   ├── repository/
│   │   │   ├── DataSetRepository.java             # 数据集仓库
│   │   │   └── AnalysisResultRepository.java      # 分析结果仓库
│   │   ├── service/
│   │   │   ├── ExcelAnalysisService.java          # Excel分析服务
│   │   │   ├── AdvancedStatisticalAnalysisService.java # 高级统计分析服务
│   │   │   └── ChartGenerationService.java        # 图表生成服务
│   │   └── ...
│   └── resources/
│       └── application.properties                 # 应用配置
└── pom.xml                                        # Maven配置
```

## 快速开始

### 环境要求
- JDK 17或更高版本
- Maven 3.6+
- 8GB+ RAM (推荐)

### 运行步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd excel-analyzer
```

2. **编译和运行**
```bash
mvn clean install
mvn spring-boot:run
```

3. **访问应用**
- 应用服务: http://localhost:8080
- H2数据库控制台: http://localhost:8080/h2-console
- 健康检查: http://localhost:8080/actuator/health

## API接口文档

### 1. 文件上传和基础分析

#### POST `/api/upload`
上传Excel文件并执行基础分析

**请求参数:**
- `file`: MultipartFile - Excel文件

**响应示例:**
```json
{
    "data": [...],
    "headers": ["列名1", "列名2"],
    "fileName": "test.xlsx",
    "totalRows": 1000,
    "totalColumns": 5,
    "advancedStatistics": {
        "列名1": {
            "columnName": "列名1",
            "dataType": "NUMERIC",
            "mean": 123.45,
            "standardDeviation": 45.67,
            "skewness": 0.12,
            "kurtosis": -0.45,
            "outliers": ["999"],
            "completenessRatio": 0.95
        }
    },
    "correlationAnalysis": {
        "correlationMatrix": {...},
        "strongCorrelations": {...}
    }
}
```

### 2. 高级分析接口

#### GET `/api/advanced/datasets`
获取所有数据集列表

#### GET `/api/advanced/datasets/summary`
获取数据集统计摘要

#### POST `/api/advanced/regression/{datasetId}`
执行回归分析

**请求体:**
```json
{
    "dependentVariable": "销售额",
    "independentVariables": ["广告支出", "促销次数"]
}
```

#### POST `/api/advanced/chart/generate`
生成图表

**请求体:**
```json
{
    "chartType": "BAR",
    "title": "销售趋势图",
    "xAxisLabel": "月份",
    "yAxisLabel": "销售额",
    "categories": ["1月", "2月", "3月"],
    "seriesData": {
        "销售额": [1000, 1200, 1500]
    },
    "width": 800,
    "height": 600
}
```

#### POST `/api/advanced/predict/{datasetId}`
执行预测分析

**请求体:**
```json
{
    "algorithm": "LINEAR_REGRESSION",
    "targetVariable": "销售额"
}
```

### 3. 支持的图表类型
- **BAR**: 柱状图
- **LINE**: 折线图
- **PIE**: 饼图
- **SCATTER**: 散点图
- **AREA**: 面积图
- **HISTOGRAM**: 直方图
- **HEATMAP**: 热力图

### 4. 支持的统计分析
- **描述性统计**: 均值、中位数、标准差、方差
- **分布分析**: 偏度、峰度、四分位数
- **相关性分析**: Pearson、Spearman相关系数
- **回归分析**: 线性回归、多元回归
- **异常值检测**: 基于IQR的异常值识别
- **数据质量评估**: 完整性、一致性分析

## 配置说明

### 数据库配置
```properties
# H2数据库配置
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### 文件上传配置
```properties
# 支持最大50MB文件
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

### 缓存配置
```properties
# Caffeine缓存配置
spring.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=30m
```

## 性能优化

### 1. 数据库索引
- 数据集表按上传时间建立索引
- 分析结果表按数据集ID和分析类型建立联合索引

### 2. 缓存策略
- 分析结果使用Caffeine缓存
- 图表数据缓存30分钟

### 3. 异步处理
- 大文件分析采用异步处理
- 批量图表生成支持并发

## 扩展功能

### 1. 机器学习算法
- 支持多种Weka算法
- 分类、回归、聚类分析
- 模型评估和验证

### 2. 数据导出
- 支持多种格式导出
- 分析报告自动生成
- 图表批量导出

### 3. 数据预处理
- 数据清洗和转换
- 缺失值处理
- 数据标准化

## 部署说明

### 1. 打包应用
```bash
mvn clean package -DskipTests
```

### 2. 运行JAR文件
```bash
java -Xmx4g -jar target/excel-analyzer-1.0.0.jar
```

### 3. Docker部署
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/excel-analyzer-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 安全配置

### 1. 基础认证
- 默认用户名: admin
- 默认密码: admin123

### 2. API安全
- 所有API接口支持跨域访问
- 文件上传大小限制
- 数据库连接池配置

## 监控和日志

### 1. 应用监控
- Spring Boot Actuator端点
- 健康检查和指标监控
- 自定义业务指标

### 2. 日志配置
- 分析过程详细日志
- 错误日志和异常跟踪
- 性能监控日志

## 许可证

MIT License

## 贡献指南

1. Fork项目
2. 创建功能分支
3. 提交代码变更
4. 创建Pull Request

## 联系方式

如有问题或建议，请通过以下方式联系：
- 邮箱: [email@example.com]
- 项目地址: [GitHub Repository URL]
