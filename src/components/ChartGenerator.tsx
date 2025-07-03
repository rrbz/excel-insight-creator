
import React, { useState, useMemo } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line, PieChart, Pie, Cell, AreaChart, Area } from 'recharts';
import { BarChart3, LineChart as LineChartIcon, PieChart as PieChartIcon, AreaChart as AreaChartIcon, Download, Settings } from 'lucide-react';
import { DataRow } from '@/pages/Index';

interface ChartGeneratorProps {
  data: DataRow[];
  headers: string[];
}

type ChartType = 'bar' | 'line' | 'pie' | 'area';

const COLORS = ['#3B82F6', '#8B5CF6', '#10B981', '#F59E0B', '#EF4444', '#06B6D4', '#84CC16', '#F97316'];

export const ChartGenerator: React.FC<ChartGeneratorProps> = ({ data, headers }) => {
  const [chartType, setChartType] = useState<ChartType>('bar');
  const [xAxis, setXAxis] = useState<string>(headers[0] || '');
  const [yAxis, setYAxis] = useState<string>('');
  const [maxDataPoints, setMaxDataPoints] = useState(20);

  // 获取数值类型的列
  const numericColumns = useMemo(() => {
    return headers.filter(header => {
      const values = data.slice(0, 100).map(row => row[header]).filter(v => v !== null && v !== undefined && v !== '');
      const numericValues = values.filter(v => typeof v === 'number' || !isNaN(Number(v)));
      return numericValues.length > values.length * 0.7;
    });
  }, [data, headers]);

  // 处理图表数据
  const chartData = useMemo(() => {
    if (!xAxis || !yAxis) return [];

    // 聚合数据
    const aggregated = data.reduce((acc, row) => {
      const xValue = String(row[xAxis] || '');
      const yValue = Number(row[yAxis]) || 0;
      
      if (xValue && !isNaN(yValue)) {
        if (!acc[xValue]) {
          acc[xValue] = { name: xValue, value: 0, count: 0 };
        }
        acc[xValue].value += yValue;
        acc[xValue].count += 1;
      }
      return acc;
    }, {} as Record<string, { name: string; value: number; count: number }>);

    // 转换为数组并排序
    const result = Object.values(aggregated)
      .map(item => ({
        name: item.name,
        value: parseFloat(item.value.toFixed(2)),
        count: item.count
      }))
      .sort((a, b) => b.value - a.value)
      .slice(0, maxDataPoints);

    return result;
  }, [data, xAxis, yAxis, maxDataPoints]);

  const renderChart = () => {
    if (!chartData.length) {
      return (
        <div className="h-96 flex items-center justify-center text-gray-500">
          <p>请选择X轴和Y轴数据来生成图表</p>
        </div>
      );
    }

    const commonProps = {
      width: '100%',
      height: 400,
      data: chartData,
      margin: { top: 20, right: 30, left: 20, bottom: 60 }
    };

    switch (chartType) {
      case 'bar':
        return (
          <ResponsiveContainer {...commonProps}>
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
              <XAxis 
                dataKey="name" 
                angle={-45} 
                textAnchor="end" 
                height={80}
                fontSize={12}
                stroke="#6B7280"
              />
              <YAxis fontSize={12} stroke="#6B7280" />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: '#1F2937', 
                  border: 'none', 
                  borderRadius: '8px',
                  color: 'white'
                }}
                formatter={(value: number) => [value.toLocaleString(), yAxis]}
              />
              <Bar dataKey="value" fill="url(#barGradient)" radius={[4, 4, 0, 0]} />
              <defs>
                <linearGradient id="barGradient" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="#3B82F6" />
                  <stop offset="100%" stopColor="#1D4ED8" />
                </linearGradient>
              </defs>
            </BarChart>
          </ResponsiveContainer>
        );

      case 'line':
        return (
          <ResponsiveContainer {...commonProps}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
              <XAxis 
                dataKey="name" 
                angle={-45} 
                textAnchor="end" 
                height={80}
                fontSize={12}
                stroke="#6B7280"
              />
              <YAxis fontSize={12} stroke="#6B7280" />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: '#1F2937', 
                  border: 'none', 
                  borderRadius: '8px',
                  color: 'white'
                }}
                formatter={(value: number) => [value.toLocaleString(), yAxis]}
              />
              <Line 
                type="monotone" 
                dataKey="value" 
                stroke="#3B82F6" 
                strokeWidth={3}
                dot={{ fill: '#3B82F6', strokeWidth: 2, r: 4 }}
                activeDot={{ r: 6, fill: '#1D4ED8' }}
              />
            </LineChart>
          </ResponsiveContainer>
        );

      case 'pie':
        return (
          <ResponsiveContainer {...commonProps}>
            <PieChart>
              <Pie
                data={chartData.slice(0, 8)}
                cx="50%"
                cy="50%"
                outerRadius={120}
                fill="#8884d8"
                dataKey="value"
                label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(1)}%`}
                labelLine={false}
              >
                {chartData.slice(0, 8).map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip formatter={(value: number) => [value.toLocaleString(), yAxis]} />
            </PieChart>
          </ResponsiveContainer>
        );

      case 'area':
        return (
          <ResponsiveContainer {...commonProps}>
            <AreaChart data={chartData}>
              <defs>
                <linearGradient id="colorArea" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#3B82F6" stopOpacity={0.8}/>
                  <stop offset="95%" stopColor="#3B82F6" stopOpacity={0.1}/>
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
              <XAxis 
                dataKey="name" 
                angle={-45} 
                textAnchor="end" 
                height={80}
                fontSize={12}
                stroke="#6B7280"
              />
              <YAxis fontSize={12} stroke="#6B7280" />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: '#1F2937', 
                  border: 'none', 
                  borderRadius: '8px',
                  color: 'white'
                }}
                formatter={(value: number) => [value.toLocaleString(), yAxis]}
              />
              <Area 
                type="monotone" 
                dataKey="value" 
                stroke="#3B82F6" 
                fillOpacity={1} 
                fill="url(#colorArea)" 
                strokeWidth={2}
              />
            </AreaChart>
          </ResponsiveContainer>
        );

      default:
        return null;
    }
  };

  const chartTypeOptions = [
    { value: 'bar', label: '柱状图', icon: BarChart3 },
    { value: 'line', label: '折线图', icon: LineChartIcon },
    { value: 'pie', label: '饼图', icon: PieChartIcon },
    { value: 'area', label: '面积图', icon: AreaChartIcon },
  ];

  return (
    <div className="space-y-6">
      {/* 图表配置 */}
      <div className="bg-gradient-to-r from-gray-50 to-blue-50 rounded-xl p-6 border border-gray-200">
        <div className="flex items-center gap-2 mb-4">
          <Settings className="w-5 h-5 text-blue-600" />
          <h3 className="text-lg font-semibold text-gray-900">图表配置</h3>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {/* 图表类型选择 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">图表类型</label>
            <div className="space-y-2">
              {chartTypeOptions.map(option => {
                const Icon = option.icon;
                return (
                  <button
                    key={option.value}
                    onClick={() => setChartType(option.value as ChartType)}
                    className={`w-full flex items-center gap-3 px-3 py-2 rounded-lg transition-all duration-200 ${
                      chartType === option.value
                        ? 'bg-blue-500 text-white shadow-md'
                        : 'bg-white text-gray-700 hover:bg-blue-50 border border-gray-200'
                    }`}
                  >
                    <Icon className="w-4 h-4" />
                    <span className="text-sm font-medium">{option.label}</span>
                  </button>
                );
              })}
            </div>
          </div>

          {/* X轴选择 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">X轴 (分类)</label>
            <select
              value={xAxis}
              onChange={(e) => setXAxis(e.target.value)}
              className="w-full px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="">选择X轴</option>
              {headers.map(header => (
                <option key={header} value={header}>{header}</option>
              ))}
            </select>
          </div>

          {/* Y轴选择 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Y轴 (数值)</label>
            <select
              value={yAxis}
              onChange={(e) => setYAxis(e.target.value)}
              className="w-full px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="">选择Y轴</option>
              {numericColumns.map(header => (
                <option key={header} value={header}>{header}</option>
              ))}
            </select>
          </div>

          {/* 数据点数量 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">最大数据点</label>
            <select
              value={maxDataPoints}
              onChange={(e) => setMaxDataPoints(Number(e.target.value))}
              className="w-full px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value={10}>10</option>
              <option value={20}>20</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
            </select>
          </div>
        </div>
      </div>

      {/* 图表展示 */}
      <div className="bg-white rounded-xl border border-gray-200 shadow-sm overflow-hidden">
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-900">
            {xAxis && yAxis ? `${yAxis} 按 ${xAxis} 分布` : '数据图表'}
          </h3>
          <button className="flex items-center gap-2 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors">
            <Download className="w-4 h-4" />
            导出图表
          </button>
        </div>
        
        <div className="p-6">
          {renderChart()}
        </div>

        {/* 数据总结 */}
        {chartData.length > 0 && (
          <div className="p-6 bg-gray-50 border-t border-gray-200">
            <h4 className="text-sm font-semibold text-gray-900 mb-3">数据总结</h4>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
              <div>
                <p className="text-gray-600">数据点数量</p>
                <p className="font-semibold text-gray-900">{chartData.length}</p>
              </div>
              <div>
                <p className="text-gray-600">最大值</p>
                <p className="font-semibold text-gray-900">
                  {Math.max(...chartData.map(d => d.value)).toLocaleString()}
                </p>
              </div>
              <div>
                <p className="text-gray-600">最小值</p>
                <p className="font-semibold text-gray-900">
                  {Math.min(...chartData.map(d => d.value)).toLocaleString()}
                </p>
              </div>
              <div>
                <p className="text-gray-600">平均值</p>
                <p className="font-semibold text-gray-900">
                  {(chartData.reduce((sum, d) => sum + d.value, 0) / chartData.length).toFixed(2)}
                </p>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
