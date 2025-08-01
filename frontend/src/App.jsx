import React, { useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { BookOpen, BarChart3, ShieldCheck, Users, Clock, CheckCircle, XCircle, LayoutDashboard, Library } from 'lucide-react';

const StyleInjector = () => {
  React.useEffect(() => {
    const style = document.createElement('style');
    style.textContent = `
      :root {
        --md-sys-color-primary: rgb(219 198 110);
        --md-sys-color-surface-tint: rgb(219 198 110);
        --md-sys-color-on-primary: rgb(58 48 0);
        --md-sys-color-primary-container: rgb(83 70 0);
        --md-sys-color-on-primary-container: rgb(248 226 135);
        --md-sys-color-secondary: rgb(209 198 161);
        --md-sys-color-on-secondary: rgb(54 48 22);
        --md-sys-color-secondary-container: rgb(78 71 42);
        --md-sys-color-on-secondary-container: rgb(238 226 188);
        --md-sys-color-tertiary: rgb(169 208 179);
        --md-sys-color-on-tertiary: rgb(20 55 35);
        --md-sys-color-tertiary-container: rgb(44 78 56);
        --md-sys-color-on-tertiary-container: rgb(197 236 206);
        --md-sys-color-error: rgb(255 180 171);
        --md-sys-color-on-error: rgb(105 0 5);
        --md-sys-color-error-container: rgb(147 0 10);
        --md-sys-color-on-error-container: rgb(255 218 214);
        --md-sys-color-background: rgb(21 19 11);
        --md-sys-color-on-background: rgb(232 226 212);
        --md-sys-color-surface: rgb(21 19 11);
        --md-sys-color-on-surface: rgb(232 226 212);
        --md-sys-color-surface-variant: rgb(75 71 57);
        --md-sys-color-on-surface-variant: rgb(205 198 180);
        --md-sys-color-outline: rgb(150 144 128);
        --md-sys-color-outline-variant: rgb(75 71 57);
        --md-sys-color-shadow: rgb(0 0 0);
        --md-sys-color-scrim: rgb(0 0 0);
        --md-sys-color-inverse-surface: rgb(232 226 212);
        --md-sys-color-inverse-on-surface: rgb(51 48 39);
        --md-sys-color-inverse-primary: rgb(109 94 15);
        --md-sys-color-primary-fixed: rgb(248 226 135);
        --md-sys-color-on-primary-fixed: rgb(34 27 0);
        --md-sys-color-primary-fixed-dim: rgb(219 198 110);
        --md-sys-color-on-primary-fixed-variant: rgb(83 70 0);
        --md-sys-color-secondary-fixed: rgb(238 226 188);
        --md-sys-color-on-secondary-fixed: rgb(33 27 4);
        --md-sys-color-secondary-fixed-dim: rgb(209 198 161);
        --md-sys-color-on-secondary-fixed-variant: rgb(78 71 42);
        --md-sys-color-tertiary-fixed: rgb(197 236 206);
        --md-sys-color-on-tertiary-fixed: rgb(0 33 15);
        --md-sys-color-tertiary-fixed-dim: rgb(169 208 179);
        --md-sys-color-on-tertiary-fixed-variant: rgb(44 78 56);
        --md-sys-color-surface-dim: rgb(21 19 11);
        --md-sys-color-surface-bright: rgb(60 57 48);
        --md-sys-color-surface-container-lowest: rgb(16 14 7);
        --md-sys-color-surface-container-low: rgb(30 27 19);
        --md-sys-color-surface-container: rgb(34 32 23);
        --md-sys-color-surface-container-high: rgb(45 42 33);
        --md-sys-color-surface-container-highest: rgb(56 53 43);
      }
      body {
        background-color: var(--md-sys-color-background);
        color: var(--md-sys-color-on-background);
        font-family: 'Inter', sans-serif;
      }
      /* Custom scrollbar for a better look */
      ::-webkit-scrollbar {
        width: 8px;
      }
      ::-webkit-scrollbar-track {
        background: var(--md-sys-color-surface-container-low);
      }
      ::-webkit-scrollbar-thumb {
        background-color: var(--md-sys-color-surface-variant);
        border-radius: 10px;
        border: 2px solid var(--md-sys-color-surface-container-low);
      }
      ::-webkit-scrollbar-thumb:hover {
        background-color: var(--md-sys-color-primary);
      }
    `;
    document.head.appendChild(style);
    return () => {
      document.head.removeChild(style);
    };
  }, []);
  return null;
};

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

const MOCK_MODERATION_BOOKS = [
  { id: 1, title: 'Хроники Нарнии', author: 'К. С. Льюис', user: 'user_jane', date: '2024-07-30' },
  { id: 2, title: 'Алхимик', author: 'Пауло Коэльо', user: 'booklover22', date: '2024-07-30' },
  { id: 3, title: 'Дюна', author: 'Фрэнк Герберт', user: 'sandworm_rider', date: '2024-07-29' },
  { id: 4, title: 'Маленький принц', author: 'Антуан де Сент-Экзюпери', user: 'pilot_antoine', date: '2024-07-28' },
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

const Sidebar = ({ activePage, setActivePage }) => {
  const navItems = [
    { id: 'dashboard', label: 'Панель управления', icon: <LayoutDashboard size={20} /> },
    { id: 'moderation', label: 'Модерация книг', icon: <ShieldCheck size={20} /> },
  ];

  return (
    <aside className="w-64 flex-shrink-0 p-4" style={{ backgroundColor: 'var(--md-sys-color-surface-container-low)' }}>
      <div className="flex items-center mb-10">
        <BookOpen size={32} style={{ color: 'var(--md-sys-color-primary)' }} className="mr-3" />
        <h1 className="text-xl font-bold" style={{ color: 'var(--md-sys-color-on-surface)' }}>Admin Panel</h1>
      </div>
      <nav>
        <ul>
          {navItems.map(item => (
            <li key={item.id}>
              <button
                onClick={() => setActivePage(item.id)}
                className={`w-full flex items-center py-3 px-4 my-1 rounded-lg transition-colors duration-200 ${
                  activePage === item.id 
                    ? 'text-black font-semibold' 
                    : 'hover:bg-opacity-50'
                }`}
                style={{
                  backgroundColor: activePage === item.id ? 'var(--md-sys-color-primary)' : 'transparent',
                  color: activePage === item.id ? 'var(--md-sys-color-on-primary)' : 'var(--md-sys-color-on-surface-variant)',
                }}
              >
                <span className="mr-3">{item.icon}</span>
                {item.label}
              </button>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  );
};


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


export default function App() {
  const [activePage, setActivePage] = useState('dashboard');

  const renderPage = () => {
    switch (activePage) {
      case 'dashboard':
        return <Dashboard />;
      case 'moderation':
        return <Moderation />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <>
      <StyleInjector />
      <div className="flex flex-row min-h-screen">
        <Sidebar activePage={activePage} setActivePage={setActivePage} />
        <main className="flex flex-col flex-1 p-8 overflow-auto">
          {renderPage()}
        </main>
      </div>
    </>
  );
}
