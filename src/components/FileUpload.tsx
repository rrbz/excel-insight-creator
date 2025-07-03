
import React, { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import * as XLSX from 'xlsx';
import { Upload, File, AlertCircle } from 'lucide-react';
import { toast } from 'sonner';
import { DataRow } from '@/pages/Index';

interface FileUploadProps {
  onDataUpload: (data: DataRow[], headers: string[], fileName: string) => void;
}

export const FileUpload: React.FC<FileUploadProps> = ({ onDataUpload }) => {
  const onDrop = useCallback((acceptedFiles: File[]) => {
    const file = acceptedFiles[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (e) => {
      try {
        const data = new Uint8Array(e.target?.result as ArrayBuffer);
        const workbook = XLSX.read(data, { type: 'array' });
        const sheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[sheetName];
        const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

        if (jsonData.length === 0) {
          toast.error('文件为空或格式不正确');
          return;
        }

        // 获取表头
        const headers = (jsonData[0] as string[]).filter(header => header && header.trim() !== '');
        
        // 处理数据行
        const dataRows: DataRow[] = [];
        for (let i = 1; i < jsonData.length; i++) {
          const row = jsonData[i] as (string | number)[];
          if (row && row.some(cell => cell !== undefined && cell !== null && cell !== '')) {
            const dataRow: DataRow = {};
            headers.forEach((header, index) => {
              const value = row[index];
              if (value !== undefined && value !== null) {
                // 尝试转换为数字，如果不能转换则保持字符串
                const numValue = Number(value);
                dataRow[header] = !isNaN(numValue) && value !== '' ? numValue : String(value);
              } else {
                dataRow[header] = '';
              }
            });
            dataRows.push(dataRow);
          }
        }

        if (dataRows.length === 0) {
          toast.error('没有找到有效的数据行');
          return;
        }

        onDataUpload(dataRows, headers, file.name);
        toast.success(`成功上传 ${file.name}，共 ${dataRows.length} 行数据`);
      } catch (error) {
        console.error('文件解析错误:', error);
        toast.error('文件解析失败，请检查文件格式');
      }
    };
    reader.readAsArrayBuffer(file);
  }, [onDataUpload]);

  const { getRootProps, getInputProps, isDragActive, isDragReject } = useDropzone({
    onDrop,
    accept: {
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': ['.xlsx'],
      'application/vnd.ms-excel': ['.xls'],
      'text/csv': ['.csv']
    },
    maxFiles: 1,
    maxSize: 10 * 1024 * 1024 // 10MB
  });

  return (
    <div
      {...getRootProps()}
      className={`
        border-2 border-dashed rounded-xl p-8 text-center cursor-pointer transition-all duration-300
        ${isDragActive && !isDragReject ? 'border-blue-400 bg-blue-50' : ''}
        ${isDragReject ? 'border-red-400 bg-red-50' : ''}
        ${!isDragActive ? 'border-gray-300 bg-gray-50 hover:border-blue-400 hover:bg-blue-50' : ''}
      `}
    >
      <input {...getInputProps()} />
      
      <div className="flex flex-col items-center gap-4">
        {isDragReject ? (
          <AlertCircle className="w-12 h-12 text-red-400" />
        ) : (
          <div className="p-4 bg-gradient-to-r from-blue-400 to-indigo-500 rounded-full">
            {isDragActive ? (
              <File className="w-8 h-8 text-white" />
            ) : (
              <Upload className="w-8 h-8 text-white" />
            )}
          </div>
        )}
        
        <div>
          {isDragReject ? (
            <p className="text-red-600 font-medium">不支持的文件格式</p>
          ) : isDragActive ? (
            <p className="text-blue-600 font-medium text-lg">释放文件以开始上传</p>
          ) : (
            <>
              <p className="text-gray-700 text-lg font-medium mb-2">
                拖拽Excel文件到此处，或点击选择文件
              </p>
              <p className="text-gray-500 text-sm">
                支持 .xlsx, .xls, .csv 格式，最大10MB
              </p>
            </>
          )}
        </div>
      </div>
    </div>
  );
};
