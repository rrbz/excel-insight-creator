
import React, { useState } from 'react';
import { ChevronLeft, ChevronRight, Search, Filter } from 'lucide-react';
import { DataRow } from '@/pages/Index';

interface DataPreviewProps {
  data: DataRow[];
  headers: string[];
}

export const DataPreview: React.FC<DataPreviewProps> = ({ data, headers }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedColumn, setSelectedColumn] = useState<string>('');
  const itemsPerPage = 10;

  // 过滤数据
  const filteredData = data.filter(row => {
    if (!searchTerm) return true;
    
    if (selectedColumn) {
      const value = String(row[selectedColumn] || '').toLowerCase();
      return value.includes(searchTerm.toLowerCase());
    } else {
      return Object.values(row).some(value => 
        String(value || '').toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
  });

  const totalPages = Math.ceil(filteredData.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const currentData = filteredData.slice(startIndex, endIndex);

  // 数据类型分析
  const getColumnType = (header: string) => {
    const values = data.slice(0, 100).map(row => row[header]).filter(v => v !== null && v !== undefined && v !== '');
    const numericValues = values.filter(v => typeof v === 'number' || !isNaN(Number(v)));
    return numericValues.length > values.length * 0.7 ? 'numeric' : 'text';
  };

  const getColumnStats = (header: string) => {
    const values = data.map(row => row[header]).filter(v => v !== null && v !== undefined && v !== '');
    const isNumeric = getColumnType(header) === 'numeric';
    
    if (isNumeric) {
      const numbers = values.map(v => Number(v)).filter(n => !isNaN(n));
      const sum = numbers.reduce((a, b) => a + b, 0);
      const avg = sum / numbers.length;
      const max = Math.max(...numbers);
      const min = Math.min(...numbers);
      return { type: 'numeric', count: numbers.length, avg: avg.toFixed(2), max, min };
    } else {
      const unique = new Set(values).size;
      return { type: 'text', count: values.length, unique };
    }
  };

  return (
    <div className="space-y-6">
      {/* 搜索和筛选 */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
          <input
            type="text"
            placeholder="搜索数据..."
            value={searchTerm}
            onChange={(e) => {
              setSearchTerm(e.target.value);
              setCurrentPage(1);
            }}
            className="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>
        <div className="relative">
          <Filter className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
          <select
            value={selectedColumn}
            onChange={(e) => {
              setSelectedColumn(e.target.value);
              setCurrentPage(1);
            }}
            className="pl-10 pr-8 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent appearance-none bg-white min-w-[200px]"
          >
            <option value="">所有列</option>
            {headers.map(header => (
              <option key={header} value={header}>{header}</option>
            ))}
          </select>
        </div>
      </div>

      {/* 数据统计卡片 */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {headers.slice(0, 4).map(header => {
          const stats = getColumnStats(header);
          return (
            <div key={header} className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg p-4 border border-blue-100">
              <h4 className="font-medium text-gray-900 truncate mb-2">{header}</h4>
              <div className="text-sm text-gray-600 space-y-1">
                <p>类型: {stats.type === 'numeric' ? '数值' : '文本'}</p>
                <p>数据量: {stats.count}</p>
                {stats.type === 'numeric' ? (
                  <>
                    <p>平均值: {(stats as any).avg}</p>
                    <p>范围: {(stats as any).min} - {(stats as any).max}</p>
                  </>
                ) : (
                  <p>唯一值: {(stats as any).unique}</p>
                )}
              </div>
            </div>
          );
        })}
      </div>

      {/* 数据表格 */}
      <div className="bg-white rounded-xl border border-gray-200 overflow-hidden shadow-sm">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gradient-to-r from-gray-50 to-gray-100">
              <tr>
                {headers.map((header, index) => (
                  <th key={index} className="px-4 py-3 text-left text-sm font-semibold text-gray-900 border-b border-gray-200">
                    <div className="flex items-center gap-2">
                      {header}
                      <span className="text-xs px-2 py-1 bg-gray-200 rounded-full">
                        {getColumnType(header) === 'numeric' ? '数值' : '文本'}
                      </span>
                    </div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {currentData.map((row, rowIndex) => (
                <tr key={rowIndex} className="hover:bg-gray-50 transition-colors duration-200">
                  {headers.map((header, colIndex) => (
                    <td key={colIndex} className="px-4 py-3 text-sm text-gray-700 border-b border-gray-100">
                      <div className="max-w-xs truncate">
                        {typeof row[header] === 'number' ? 
                          row[header].toLocaleString() : 
                          String(row[header] || '-')
                        }
                      </div>
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* 分页控制 */}
      {totalPages > 1 && (
        <div className="flex items-center justify-between">
          <p className="text-sm text-gray-600">
            显示 {startIndex + 1} - {Math.min(endIndex, filteredData.length)} 条，共 {filteredData.length} 条数据
          </p>
          <div className="flex items-center gap-2">
            <button
              onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
              disabled={currentPage === 1}
              className="p-2 rounded-lg border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              <ChevronLeft className="w-4 h-4" />
            </button>
            <span className="px-4 py-2 text-sm font-medium bg-blue-50 text-blue-700 rounded-lg">
              {currentPage} / {totalPages}
            </span>
            <button
              onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
              disabled={currentPage === totalPages}
              className="p-2 rounded-lg border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
            >
              <ChevronRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
