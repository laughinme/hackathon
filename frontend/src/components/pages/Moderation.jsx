import React, { useState } from 'react';
import { CheckCircle, XCircle, ShieldCheck } from 'lucide-react';

const MOCK_MODERATION_BOOKS = [
  { id: 1, title: 'Хроники Нарнии', author: 'К. С. Льюис', user: 'user_jane', date: '2024-07-30' },
  { id: 2, title: 'Алхимик', author: 'Пауло Коэльо', user: 'booklover22', date: '2024-07-30' },
  { id: 3, title: 'Дюна', author: 'Фрэнк Герберт', user: 'sandworm_rider', date: '2024-07-29' },
  { id: 4, title: 'Маленький принц', author: 'Антуан де Сент-Экзюпери', user: 'pilot_antoine', date: '2024-07-28' },
];

const Moderation = () => {
  const [books, setBooks] = useState(MOCK_MODERATION_BOOKS);

  const handleAction = (id, action) => {
    console.log(`${action} book with id: ${id}`);
    setBooks(books.filter(book => book.id !== id));
  };

  return (
    <div>
      <h2 className="text-3xl font-bold mb-6" style={{ color: 'var(--md-sys-color-on-surface)' }}>Модерация книг</h2>
      <div className="rounded-xl overflow-hidden" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)' }}>
              <tr>
                <th className="p-4 font-semibold" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Название</th>
                <th className="p-4 font-semibold" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Автор</th>
                <th className="p-4 font-semibold" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Пользователь</th>
                <th className="p-4 font-semibold" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Дата добавления</th>
                <th className="p-4 font-semibold text-right" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Действия</th>
              </tr>
            </thead>
            <tbody>
              {books.map(book => (
                <tr key={book.id} className="border-t" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
                  <td className="p-4">
                    <p className="font-semibold" style={{ color: 'var(--md-sys-color-on-surface)' }}>{book.title}</p>
                  </td>
                  <td className="p-4">
                    <p className="text-sm" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{book.author}</p>
                  </td>
                  <td className="p-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{book.user}</td>
                  <td className="p-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{book.date}</td>
                  <td className="p-4 text-right">
                    <div className="flex justify-end space-x-2">
                      <button
                        onClick={() => handleAction(book.id, 'Approved')}
                        className="flex items-center px-3 py-1.5 text-sm font-medium rounded-md transition-colors"
                        style={{ backgroundColor: 'var(--md-sys-color-tertiary-container)', color: 'var(--md-sys-color-on-tertiary-container)' }}
                      >
                        <CheckCircle size={16} className="mr-1.5" />
                        Одобрить
                      </button>
                      <button
                        onClick={() => handleAction(book.id, 'Rejected')}
                        className="flex items-center px-3 py-1.5 text-sm font-medium rounded-md transition-colors"
                        style={{ backgroundColor: 'var(--md-sys-color-error-container)', color: 'var(--md-sys-color-on-error-container)' }}
                      >
                         <XCircle size={16} className="mr-1.5" />
                        Отклонить
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {books.length === 0 && (
            <div className="text-center p-8" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
              <ShieldCheck size={48} className="mx-auto mb-4" />
              <p className="text-lg">Все книги проверены!</p>
              <p>Новых книг на модерацию пока нет.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Moderation;