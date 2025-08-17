import React, { useEffect, useState } from 'react';
import { CheckCircle, XCircle, ShieldCheck } from 'lucide-react';
import { adminListBooks, adminAcceptBook, adminRejectBook } from '../../api/admin';

const Moderation = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadBooks = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await adminListBooks({ status: 'pending', limit: 50 });
      setBooks(Array.isArray(data) ? data : []);
    } catch (e) {
      setError('Не удалось загрузить книги на модерации');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadBooks();
  }, []);

  const approve = async (id) => {
    try {
      await adminAcceptBook(id);
      setBooks(prev => prev.filter(b => b.id !== id));
    } catch (e) {
      // ignore
    }
  };

  const reject = async (id) => {
    const reason = prompt('Укажите причину отклонения');
    if (reason === null) return;
    try {
      await adminRejectBook(id, reason || '');
      setBooks(prev => prev.filter(b => b.id !== id));
    } catch (e) {
      // ignore
    }
  };

  return (
    <div>
      <h2 className="text-3xl font-bold mb-6" style={{ color: 'var(--md-sys-color-on-surface)' }}>Модерация книг</h2>

      {error && <div className="mb-4 p-3 rounded" style={{ backgroundColor: 'var(--md-sys-color-error-container)', color: 'var(--md-sys-color-on-error-container)' }}>{error}</div>}

      <div className="rounded-xl overflow-hidden" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        {loading ? (
          <div className="p-8">Загрузка...</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left">
              <thead style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)' }}>
                <tr>
                  <th className="p-4 font-semibold" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Название</th>
                  <th className="p-4 font-semibold" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Автор</th>
                  <th className="p-4 font-semibold" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Владелец</th>
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
                      <p className="text-sm" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{book.author?.name || book.author?.title || ''}</p>
                    </td>
                    <td className="p-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{book.owner?.username || book.owner?.email}</td>
                    <td className="p-4 text-right">
                      <div className="flex justify-end space-x-2">
                        <button
                          onClick={() => approve(book.id)}
                          className="flex items-center px-3 py-1.5 text-sm font-medium rounded-md transition-colors"
                          style={{ backgroundColor: 'var(--md-sys-color-tertiary-container)', color: 'var(--md-sys-color-on-tertiary-container)' }}
                        >
                          <CheckCircle size={16} className="mr-1.5" />
                          Одобрить
                        </button>
                        <button
                          onClick={() => reject(book.id)}
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
        )}
      </div>
    </div>
  );
};

export default Moderation;