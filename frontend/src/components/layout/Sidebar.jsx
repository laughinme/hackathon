import React, { useContext } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { BookOpen, LayoutDashboard, Users, Repeat } from 'lucide-react';
import AuthContext from '../../context/AuthContext';

const Sidebar = () => {
  const { logout } = useContext(AuthContext);
  const location = useLocation();

  const navItems = [
    { id: 'dashboard', label: 'Панель управления', icon: <LayoutDashboard size={20} />, path: '/' },
    { id: 'books', label: 'Книги', icon: <BookOpen size={20} />, path: '/books' },
    { id: 'users', label: 'Пользователи', icon: <Users size={20} />, path: '/users' },
    { id: 'exchanges', label: 'Обмены', icon: <Repeat size={20} />, path: '/exchanges' },
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
              <Link
                to={item.path}
                className={`w-full flex items-center py-3 px-4 my-1 rounded-lg transition-colors duration-200 ${
                  location.pathname === item.path
                    ? 'text-black font-semibold'
                    : 'hover:bg-opacity-50'
                }`}
                style={{
                  backgroundColor: location.pathname === item.path ? 'var(--md-sys-color-primary)' : 'transparent',
                  color: location.pathname === item.path ? 'var(--md-sys-color-on-primary)' : 'var(--md-sys-color-on-surface-variant)',
                }}
              >
                <span className="mr-3">{item.icon}</span>
                {item.label}
              </Link>
            </li>
          ))}
        </ul>
      </nav>
      <div className="mt-auto">
        <button
          onClick={logout}
          className="w-full flex items-center justify-center py-3 px-4 my-1 rounded-lg transition-colors duration-200"
          style={{
            backgroundColor: 'var(--md-sys-color-error-container)',
            color: 'var(--md-sys-color-on-error-container)',
          }}
        >
          Выйти
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;