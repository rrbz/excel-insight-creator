
import React, { useState } from 'react';
import { FileUpload } from '@/components/FileUpload';
import { DataPreview } from '@/components/DataPreview';
import { ChartGenerator } from '@/components/ChartGenerator';
import { BarChart3, FileSpreadsheet, TrendingUp } from 'lucide-react';

export interface DataRow {
  [key: string]: string | number;
}

const Index = () => {
  const [data, setData] = useState<DataRow[]>([]);
  const [headers, setHeaders] = useState<string[]>([]);
  const [fileName, setFileName] = useState<string>('');

  const handleDataUpload = (uploadedData: DataRow[], uploadedHeaders: string[], name: string) => {
    setData(uploadedData);
    setHeaders(uploadedHeaders);
    setFileName(name);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 via-blue-50 to-indigo-100">
      {/* Header */}
      <div className="bg-white/80 backdrop-blur-sm border-b border-gray-200/50 sticky top-0 z-50">
        <div className="container mx-auto px-6 py-4">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-lg">
              <FileSpreadsheet className="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 className="text-2xl font-bold bg-gradient-to-r from-gray-900 to-gray-600 bg-clip-text text-transparent">
                Excel 数据分析系统
              </h1>
              <p className="text-sm text-gray-600">上传Excel文件，生成专业图表分析</p>
            </div>
          </div>
        </div>
      </div>

      <div className="container mx-auto px-6 py-8">
        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white/70 backdrop-blur-sm rounded-2xl p-6 border border-gray-200/50 shadow-lg hover:shadow-xl transition-all duration-300">
            <div className="flex items-center gap-4">
              <div className="p-3 bg-gradient-to-r from-blue-400 to-blue-600 rounded-full">
                <FileSpreadsheet className="w-6 h-6 text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-600">数据行数</p>
                <p className="text-2xl font-bold text-gray-900">{data.length.toLocaleString()}</p>
              </div>
            </div>
          </div>
          
          <div className="bg-white/70 backdrop-blur-sm rounded-2xl p-6 border border-gray-200/50 shadow-lg hover:shadow-xl transition-all duration-300">
            <div className="flex items-center gap-4">
              <div className="p-3 bg-gradient-to-r from-indigo-400 to-indigo-600 rounded-full">
                <BarChart3 className="w-6 h-6 text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-600">数据列数</p>
                <p className="text-2xl font-bold text-gray-900">{headers.length}</p>
              </div>
            </div>
          </div>
          
          <div className="bg-white/70 backdrop-blur-sm rounded-2xl p-6 border border-gray-200/50 shadow-lg hover:shadow-xl transition-all duration-300">
            <div className="flex items-center gap-4">
              <div className="p-3 bg-gradient-to-r from-purple-400 to-purple-600 rounded-full">
                <TrendingUp className="w-6 h-6 text-white" />
              </div>
              <div>
                <p className="text-sm text-gray-600">文件状态</p>
                <p className="text-lg font-semibold text-gray-900">
                  {fileName ? '已加载' : '未上传'}
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Main Content */}
        <div className="space-y-8">
          {/* File Upload Section */}
          <div className="bg-white/70 backdrop-blur-sm rounded-2xl p-8 border border-gray-200/50 shadow-lg">
            <h2 className="text-xl font-semibold text-gray-900 mb-6 flex items-center gap-2">
              <FileSpreadsheet className="w-5 h-5 text-blue-600" />
              上传Excel文件
            </h2>
            <FileUpload onDataUpload={handleDataUpload} />
            {fileName && (
              <div className="mt-4 p-4 bg-green-50 border border-green-200 rounded-lg">
                <p className="text-green-800 font-medium">已成功加载: {fileName}</p>
              </div>
            )}
          </div>

          {/* Data Preview Section */}
          {data.length > 0 && (
            <div className="bg-white/70 backdrop-blur-sm rounded-2xl p-8 border border-gray-200/50 shadow-lg">
              <h2 className="text-xl font-semibold text-gray-900 mb-6 flex items-center gap-2">
                <BarChart3 className="w-5 h-5 text-blue-600" />
                数据预览
              </h2>
              <DataPreview data={data} headers={headers} />
            </div>
          )}

          {/* Chart Generation Section */}
          {data.length > 0 && (
            <div className="bg-white/70 backdrop-blur-sm rounded-2xl p-8 border border-gray-200/50 shadow-lg">
              <h2 className="text-xl font-semibold text-gray-900 mb-6 flex items-center gap-2">
                <TrendingUp className="w-5 h-5 text-blue-600" />
                图表生成
              </h2>
              <ChartGenerator data={data} headers={headers} />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Index;
