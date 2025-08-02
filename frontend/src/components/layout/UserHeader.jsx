import React from 'react';
import { Book, Search, Map, Library, User } from 'lucide-react';
import { Link } from 'react-router-dom';

const UserHeader = () => {
  return (
    <header className="w-full p-4 flex items-center" style={{ backgroundColor: 'var(--md-sys-color-surface-container-low)' }}>
      <div className="flex items-center mr-8">
        <Book size={28} style={{ color: 'var(--md-sys-color-primary)' }} />
        <span className="text-xl font-bold ml-2" style={{ color: 'var(--md-sys-color-on-surface)' }}>BOOK-EXCHANGE 2.0</span>
      </div>

      <div className="flex-grow max-w-lg relative">
        <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
        <input
          type="text"
          placeholder="Поиск книг по названию, автору..."
          className="w-full p-2 pl-10 rounded-lg border focus:outline-none focus:ring-2"
          style={{
            backgroundColor: 'var(--md-sys-color-surface-container-high)',
            borderColor: 'var(--md-sys-color-outline-variant)',
            color: 'var(--md-sys-color-on-surface)',
            '--tw-ring-color': 'var(--md-sys-color-primary)',
          }}
        />
      </div>

      <nav className="flex items-center ml-auto space-x-6">
        <a href="#" className="flex items-center space-x-2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
          <Map size={20} />
          <span>Карта точек</span>
        </a>
        <a href="#" className="flex items-center space-x-2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
          <Library size={20} />
          <span>Мои книги</span>
        </a>
        <Link to="/profile" className="flex items-center space-x-2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
          <User size={20} />
          <span>Профиль</span>
        </Link>
      </nav>
    </header>
  );
};

export default UserHeader;