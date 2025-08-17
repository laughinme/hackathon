import React, { useEffect, useState } from 'react';
import { adminListExchanges, adminGetExchange, adminForceCancelExchange, adminForceFinishExchange } from '../../api/admin';

const ExchangesPage = () => {
  const [items, setItems] = useState([]);
  const [cursor, setCursor] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async (reset = false) => {
    setLoading(true);
    setError('');
    try {
      const data = await adminListExchanges({ limit: 20, cursor: reset ? null : cursor });
      setItems(reset ? data.items : [...items, ...data.items]);
      setCursor(data.next_cursor);
    } catch (e) {
      setError('Не удалось загрузить обмены');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(true); }, []);

  const openInfo = async (id) => {
    try {
      const ex = await adminGetExchange(id);
      alert(`${ex.id}\nСтатус: ${ex.progress || ex.status}\nКнига: ${ex.book?.title || ''}`);
    } catch (e) {}
  };

  const forceFinish = async (id) => {
    try { await adminForceFinishExchange(id); setItems(prev => prev.map(i => i.id === id ? { ...i, progress: 'finished' } : i)); } catch (e) {}
  };
  const forceCancel = async (id) => {
    try { await adminForceCancelExchange(id); setItems(prev => prev.map(i => i.id === id ? { ...i, progress: 'canceled' } : i)); } catch (e) {}
  };

  return (
    <div>
      <h2 className="text-3xl font-bold mb-6" style={{ color: 'var(--md-sys-color-on-surface)' }}>Обмены</h2>
      {error && <div className="mb-4 p-3 rounded" style={{ backgroundColor: 'var(--md-sys-color-error-container)', color: 'var(--md-sys-color-on-error-container)' }}>{error}</div>}
      <div className="rounded-xl overflow-hidden" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <table className="w-full text-left">
          <thead style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)' }}>
            <tr>
              <th className="p-4">ID</th>
              <th className="p-4">Статус</th>
              <th className="p-4">Книга</th>
              <th className="p-4 text-right">Действия</th>
            </tr>
          </thead>
          <tbody>
            {items.map(e => (
              <tr key={e.id} className="border-t" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
                <td className="p-4">{e.id}</td>
                <td className="p-4">{e.progress || e.status}</td>
                <td className="p-4">{e.book?.title || '—'}</td>
                <td className="p-4 text-right space-x-2">
                  <button onClick={() => openInfo(e.id)} className="px-3 py-1 rounded" style={{ backgroundColor: 'var(--md-sys-color-secondary-container)', color: 'var(--md-sys-color-on-secondary-container)' }}>Подробнее</button>
                  <button onClick={() => forceFinish(e.id)} className="px-3 py-1 rounded" style={{ backgroundColor: 'var(--md-sys-color-tertiary-container)', color: 'var(--md-sys-color-on-tertiary-container)' }}>Завершить</button>
                  <button onClick={() => forceCancel(e.id)} className="px-3 py-1 rounded" style={{ backgroundColor: 'var(--md-sys-color-error-container)', color: 'var(--md-sys-color-on-error-container)' }}>Отменить</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <div className="p-4 flex justify-center">
          <button disabled={!cursor || loading} onClick={() => load(false)} className="px-4 rounded disabled:opacity-50" style={{ backgroundColor: 'var(--md-sys-color-secondary)', color: 'var(--md-sys-color-on-secondary)' }}>Загрузить ещё</button>
        </div>
      </div>
    </div>
  );
};

export default ExchangesPage;


