import React, { useEffect, useMemo, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { Library, Clock, Users, BarChart3, ShieldCheck } from 'lucide-react';
import { adminListBooks, adminListUsers, adminListExchanges, adminStatsBooks, adminStatsActiveUsers, adminStatsRegistrations } from '../../api/admin';

const DEFAULT_STATS = {
  totalBooks: 0,
  pendingModeration: 0,
  totalUsers: 0,
  activeExchanges: 0,
};

const toLocaleDay = (isoDate) => {
  const d = new Date(isoDate);
  return d.toLocaleDateString('ru-RU', { day: '2-digit', month: '2-digit' });
};

const StatCard = ({ icon, title, value, color }) => (
  <div className="flex flex-col items-start p-4 rounded-xl flex-1" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
    <div className="p-3 mb-4 text-white rounded-lg" style={{ backgroundColor: color }}>
      {icon}
    </div>
    <div>
      <p className="text-sm font-medium" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{title}</p>
      <p className="text-2xl font-bold" style={{ color: 'var(--md-sys-color-on-surface)' }}>{value}</p>
    </div>
  </div>
);


const Dashboard = () => {
  const [stats, setStats] = useState(DEFAULT_STATS);
  const [booksChart, setBooksChart] = useState([]);

  useEffect(() => {
    // Load top-level metrics
    const load = async () => {
      try {
        const [booksPending, usersPage, exchangesPage, booksStats] = await Promise.all([
          adminListBooks({ status: 'pending', limit: 1 }),
          adminListUsers({ limit: 1 }),
          adminListExchanges({ limit: 1 }),
          adminStatsBooks(30),
        ]);

        setStats({
          totalBooks: 0, // Unknown count endpoint; keep 0 for now
          pendingModeration: Array.isArray(booksPending) ? booksPending.length : 0,
          totalUsers: usersPage?.items?.length ?? 0,
          activeExchanges: exchangesPage?.items?.length ?? 0,
        });

        const chartData = (booksStats || []).map(p => ({ name: toLocaleDay(p.day), 'Просмотры': p.views, 'Лайки': p.likes, 'Брони': p.reserves }));
        setBooksChart(chartData);
      } catch (e) {
        // ignore errors on dashboard
      }
    };
    load();
  }, []);

  return (
    <div className="flex flex-col h-full">
      <h2 className="text-3xl font-bold mb-6" style={{ color: 'var(--md-sys-color-on-surface)' }}>Панель управления</h2>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard icon={<Library size={24} />} title="Всего книг" value={stats.totalBooks.toLocaleString('ru-RU')} color="var(--md-sys-color-primary-container)" />
        <StatCard icon={<Clock size={24} />} title="На модерации" value={stats.pendingModeration} color="var(--md-sys-color-secondary-container)" />
        <StatCard icon={<Users size={24} />} title="Всего пользователей" value={stats.totalUsers.toLocaleString('ru-RU')} color="var(--md-sys-color-terтиary-container)" />
        <StatCard icon={<BarChart3 size={24} />} title="Активных обменов" value={stats.activeExchanges} color="var(--md-sys-color-error-container)" />
      </div>

      <div className="p-6 rounded-xl flex-grow" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <h3 className="text-xl font-semibold mb-4" style={{ color: 'var(--md-sys-color-on-surface)' }}>Статистика книг</h3>
        <div style={{ width: '100%', height: 300 }}>
          <ResponsiveContainer>
            <BarChart data={booksChart} margin={{ top: 5, right: 20, left: -10, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" stroke="var(--md-sys-color-outline-variant)" />
              <XAxis dataKey="name" tick={{ fill: 'var(--md-sys-color-on-surface-variant)' }} />
              <YAxis tick={{ fill: 'var(--md-sys-color-on-surface-variant)' }} />
              <Tooltip
                contentStyle={{
                  backgroundColor: 'var(--md-sys-color-surface-container-high)',
                  borderColor: 'var(--md-sys-color-outline)',
                  color: 'var(--md-sys-color-on-surface)'
                }}
                cursor={{ fill: 'var(--md-sys-color-surface-variant)', fillOpacity: 0.5 }}
              />
              <Legend wrapperStyle={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
              <Bar dataKey="Просмотры" fill="var(--md-sys-color-primary)" radius={[4, 4, 0, 0]} />
              <Bar dataKey="Лайки" fill="var(--md-sys-color-tertiary)" radius={[4, 4, 0, 0]} />
              <Bar dataKey="Брони" fill="var(--md-sys-color-secondary)" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;