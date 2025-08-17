import React, { useEffect, useState } from 'react';
import { adminListUsers, adminSetUserBan } from '../../api/admin';

const UsersPage = () => {
  const [items, setItems] = useState([]);
  const [cursor, setCursor] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [banned, setBanned] = useState(null);

  const load = async (reset = false) => {
    setLoading(true);
    setError('');
    try {
      const params = { limit: 20, cursor: reset ? null : cursor, search: search || undefined, banned: banned === null ? undefined : banned };
      const data = await adminListUsers(params);
      setItems(reset ? data.items : [...items, ...data.items]);
      setCursor(data.next_cursor);
    } catch (e) {
      setError('Не удалось загрузить пользователей');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load(true);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [search, banned]);

  const toggleBan = async (user) => {
    try {
      const updated = await adminSetUserBan(user.id, !user.banned);
      setItems(prev => prev.map(i => i.id === user.id ? updated : i));
    } catch (e) {}
  };

  return (
    <div>
      <h2 className="text-3xl font-bold mb-6" style={{ color: 'var(--md-sys-color-on-surface)' }}>Пользователи</h2>

      <div className="flex gap-3 mb-4">
        <input value={search} onChange={e => setSearch(e.target.value)} placeholder="Поиск..." className="p-2 rounded" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)', color: 'var(--md-sys-color-on-surface)' }} />
        <select value={banned === null ? '' : banned ? '1' : '0'} onChange={(e) => setBanned(e.target.value === '' ? null : e.target.value === '1')} className="p-2 rounded" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)', color: 'var(--md-sys-color-on-surface)' }}>
          <option value="">Все</option>
          <option value="0">Активные</option>
          <option value="1">Забаненные</option>
        </select>
        <button onClick={() => load(true)} className="px-4 rounded" style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>Обновить</button>
      </div>

      {error && <div className="mb-4 p-3 rounded" style={{ backgroundColor: 'var(--md-sys-color-error-container)', color: 'var(--md-sys-color-on-error-container)' }}>{error}</div>}

      <div className="rounded-xl overflow-hidden" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <table className="w-full text-left">
          <thead style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)' }}>
            <tr>
              <th className="p-4">Email</th>
              <th className="p-4">Имя</th>
              <th className="p-4">Город</th>
              <th className="p-4 text-right">Действия</th>
            </tr>
          </thead>
          <tbody>
            {items.map(u => (
              <tr key={u.id} className="border-t" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
                <td className="p-4">{u.email}</td>
                <td className="p-4">{u.username || '—'}</td>
                <td className="p-4">{u.city?.name || '—'}</td>
                <td className="p-4 text-right">
                  <button onClick={() => toggleBan(u)} className="px-3 py-1 rounded" style={{ backgroundColor: u.banned ? 'var(--md-sys-color-tertiary-container)' : 'var(--md-sys-color-error-container)', color: u.banned ? 'var(--md-sys-color-on-tertiary-container)' : 'var(--md-sys-color-on-error-container)' }}>
                    {u.banned ? 'Разбанить' : 'Забанить'}
                  </button>
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

export default UsersPage;


