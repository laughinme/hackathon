import React from 'react';
import { ArrowLeft } from 'lucide-react';

const AddBookPage = ({ onBack }) => {
  return (
    <div className="p-8">
      <button onClick={onBack} className="flex items-center mb-6 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
        <ArrowLeft size={20} className="mr-2" />
        Назад к каталогу
      </button>
      <h2 className="text-3xl font-bold mb-6" style={{ color: 'var(--md-sys-color-on-surface)' }}>Добавление новой книги</h2>
      <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <p>Здесь будет форма для добавления книги...</p>
      </div>
    </div>
  );
};

export default AddBookPage;