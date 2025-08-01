import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { Library, Clock, Users, BarChart3, ShieldCheck } from 'lucide-react';
const MOCK_STATS = {
  totalBooks: 10247,
  pendingModeration: 12,
  totalUsers: 2384,
  activeExchanges: 127,
};

const MOCK_CHART_DATA = [
  { name: 'Пн', "Новые книги": 25 },
  { name: 'Вт', "Новые книги": 30 },
  { name: 'Ср', "Новые книги": 22 },
  { name: 'Чт', "Новые книги": 45 },
  { name: 'Пт', "Новые книги": 50 },
  { name: 'Сб', "Новые книги": 38 },
  { name: 'Вс', "Новые книги": 42 },
];

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


const Dashboard = () => (
  <div className="flex flex-col h-full">
    <h2 className="text-3xl font-bold mb-6" style={{ color: 'var(--md-sys-color-on-surface)' }}>Панель управления</h2>

    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
      <StatCard icon={<Library size={24} />} title="Всего книг" value={MOCK_STATS.totalBooks.toLocaleString('ru-RU')} color="var(--md-sys-color-primary-container)" />
      <StatCard icon={<Clock size={24} />} title="На модерации" value={MOCK_STATS.pendingModeration} color="var(--md-sys-color-secondary-container)" />
      <StatCard icon={<Users size={24} />} title="Всего пользователей" value={MOCK_STATS.totalUsers.toLocaleString('ru-RU')} color="var(--md-sys-color-tertiary-container)" />
      <StatCard icon={<BarChart3 size={24} />} title="Активных обменов" value={MOCK_STATS.activeExchanges} color="var(--md-sys-color-error-container)" />
    </div>

    <div className="p-6 rounded-xl flex-grow" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
      <h3 className="text-xl font-semibold mb-4" style={{ color: 'var(--md-sys-color-on-surface)' }}>Динамика добавления книг</h3>
      <div style={{ width: '100%', height: 300 }}>
        <ResponsiveContainer>
          <BarChart data={MOCK_CHART_DATA} margin={{ top: 5, right: 20, left: -10, bottom: 5 }}>
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
            <Bar dataKey="Новые книги" fill="var(--md-sys-color-primary)" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  </div>
);

export default Dashboard;