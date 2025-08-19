import React, { useEffect, useState, useCallback } from 'react';
import { adminListExchanges, adminForceFinishExchange, adminForceCancelExchange } from '../../api/admin';
import { Loader2, Search, RefreshCw, Award, X, Clock, User, BookOpen } from 'lucide-react';

const StatusPill = ({ progress }) => {
  const map = {
    created: { text: 'Ожидает', className: 'bg-yellow-500/20 text-yellow-300' },
    accepted: { text: 'Принят', className: 'bg-blue-500/20 text-blue-300' },
    declined: { text: 'Отклонен', className: 'bg-red-500/20 text-red-300' },
    finished: { text: 'Завершен', className: 'bg-green-500/20 text-green-300' },
    canceled: { text: 'Отменен', className: 'bg-gray-500/20 text-gray-300' },
  };
  const s = map[progress] || { text: progress, className: 'bg-gray-500/20 text-gray-300' };
  return <span className={`text-xs font-medium px-2 py-1 rounded-full ${s.className}`}>{s.text}</span>;
};

const AdminExchangesPage = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [query, setQuery] = useState('');
  const [status, setStatus] = useState('all');

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const params = { limit: 50 };
      if (status !== 'all') params.status = status;
      if (query.trim()) params.query = query.trim();
      const data = await adminListExchanges(params);
      setItems(data.items || data);
    } catch (e) {
      console.error('Failed to load exchanges', e);
      setError('Не удалось загрузить обмены');
    } finally {
      setLoading(false);
    }
  }, [query, status]);

  useEffect(() => {
    load();
  }, [load]);

  const handleForceFinish = async (id) => {
    if (!confirm('Подтвердите принудительное завершение обмена')) return;
    try {
      await adminForceFinishExchange(id);
      load();
    } catch (e) {
      alert('Ошибка: не удалось завершить обмен');
    }
  };

  const handleForceCancel = async (id) => {
    if (!confirm('Подтвердите принудительную отмену обмена')) return;
    try {
      await adminForceCancelExchange(id);
      load();
    } catch (e) {
      alert('Ошибка: не удалось отменить обмен');
    }
  };

  return (
    <div className="w-full max-w-6xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Обмены пользователей</h1>
        <button onClick={load} className="flex items-center gap-2 px-3 py-2 rounded-lg" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)', color: 'var(--md-sys-color-on-surface)'}}>
          <RefreshCw size={16} /> Обновить
        </button>
      </div>

      <div className="flex flex-col md:flex-row gap-3 md:items-center">
        <div className="flex-1 relative">
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && load()}
            placeholder="Поиск по книге/пользователю"
            className="w-full p-3 pl-10 rounded-lg border"
            style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)', borderColor: 'var(--md-sys-color-outline-variant)' }}
          />
          <Search size={18} className="absolute left-3 top-1/2 -translate-y-1/2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
        </div>
        <select
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          className="p-3 rounded-lg border"
          style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)', borderColor: 'var(--md-sys-color-outline-variant)' }}
        >
          <option value="all">Все статусы</option>
          <option value="created">Ожидает</option>
          <option value="accepted">Принят</option>
          <option value="declined">Отклонен</option>
          <option value="finished">Завершен</option>
          <option value="canceled">Отменен</option>
        </select>
        <button onClick={load} className="px-4 py-2 rounded-lg font-semibold" style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>Фильтровать</button>
      </div>

      {loading && (
        <div className="p-8 text-center flex items-center justify-center gap-2"><Loader2 className="animate-spin" /> Загрузка...</div>
      )}
      {error && !loading && (
        <div className="p-8 text-center" style={{ color: 'var(--md-sys-color-error)' }}>{error}</div>
      )}

      {!loading && !error && (
        <div className="space-y-3">
          {items.length === 0 && (
            <div className="p-8 text-center rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
              Нет обменов
            </div>
          )}
          {items.map((ex) => (
            <div key={ex.id} className="p-4 rounded-xl flex gap-4 items-start" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
              <img src={ex.book?.photo_urls?.[0] || `https://placehold.co/200x300/3A342B/E8E2D4?text=No+Image`} alt={ex.book?.title} className="w-20 h-28 rounded-lg object-cover" />
              <div className="flex-1 min-w-0">
                <div className="flex items-center justify-between gap-2">
                  <div className="min-w-0">
                    <div className="flex items-center gap-2">
                      <BookOpen size={16} />
                      <h3 className="font-semibold truncate">{ex.book?.title}</h3>
                    </div>
                    <div className="mt-1 text-sm" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{ex.book?.author?.name}</div>
                  </div>
                  <StatusPill progress={ex.progress} />
                </div>
                <div className="mt-2 text-sm grid grid-cols-1 md:grid-cols-3 gap-2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
                  <div className="flex items-center gap-2"><User size={14} />Владелец: {ex.owner?.username}</div>
                  <div className="flex items-center gap-2"><User size={14} />Запросивший: {ex.requester?.username}</div>
                  <div className="flex items-center gap-2"><Clock size={14} />{new Date(ex.created_at).toLocaleString()}</div>
                </div>
                <div className="mt-3 flex gap-2 flex-wrap">
                  {(ex.progress === 'accepted') && (
                    <button onClick={() => handleForceFinish(ex.id)} className="flex items-center gap-2 px-3 py-2 rounded-lg text-sm" style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
                      <Award size={16} /> Принудительно завершить
                    </button>
                  )}
                  {(ex.progress === 'created' || ex.progress === 'accepted') && (
                    <button onClick={() => handleForceCancel(ex.id)} className="flex items-center gap-2 px-3 py-2 rounded-lg text-sm" style={{ backgroundColor: 'var(--md-sys-color-error-container)', color: 'var(--md-sys-color-on-error-container)' }}>
                      <X size={16} /> Принудительно отменить
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AdminExchangesPage;


