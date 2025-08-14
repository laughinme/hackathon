import React, { useContext } from 'react';
import { Book, Map, Mail, User, LogOut, Plus } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../App';

const UserHeader = () => {
  const { currentUser, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  return (
    <header className="w-full p-4 flex items-center justify-between gap-4" style={{ backgroundColor: 'var(--md-sys-color-surface-container-low)' }}>
      <Link to="/home" className="flex items-center flex-shrink-0">
        <Book size={28} style={{ color: 'var(--md-sys-color-primary)' }} />
        <span className="text-xl font-bold ml-2 hidden md:inline" style={{ color: 'var(--md-sys-color-on-surface)' }}>BOOK-EXCHANGE</span>
      </Link>

      <nav className="flex items-center ml-auto space-x-2 md:space-x-4">
        <button 
          onClick={() => navigate('/add-book')} 
          className="hidden sm:flex items-center px-3 py-2 rounded-lg font-semibold text-sm gap-2" 
          style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
          <Plus size={16}/> Добавить книгу
        </button>

        <Link to="/map" title="Карта точек" className="p-2 rounded-full hover:bg-[var(--md-sys-color-surface-container-high)]">
          <Map size={22} style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
        </Link>
        
        <Link to="/exchanges" title="Мои обмены" className="p-2 rounded-full hover:bg-[var(--md-sys-color-surface-container-high)]">
          <Mail size={22} style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
        </Link>

        <div className="group relative">
            <button onClick={() => navigate('/profile')} className="block">
                <img 
                    src={currentUser?.avatar_url || `https://placehold.co/80x80/DBC66E/3A3000?text=${currentUser?.username?.[0] || '?'}`} 
                    alt="User Avatar" 
                    className="w-10 h-10 rounded-full object-cover cursor-pointer border-2 border-transparent group-hover:border-[var(--md-sys-color-primary)]"
                />
            </button>
          <div className="absolute right-0 mt-2 w-56 rounded-md shadow-lg py-1 z-50 hidden group-hover:block" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)'}}>
            <div className="px-4 py-2 border-b border-[var(--md-sys-color-outline-variant)]">
                <p className="text-sm font-semibold truncate">{currentUser?.username}</p>
                <p className="text-xs text-[var(--md-sys-color-on-surface-variant)] truncate">{currentUser?.email}</p>
            </div>
            <Link to="/profile" className="flex w-full text-left items-center gap-3 px-4 py-2 text-sm hover:bg-[var(--md-sys-color-surface-variant)]">
              <User size={16} /> Профиль и мои книги
            </Link>
            <button onClick={logout} className="w-full text-left flex items-center gap-3 px-4 py-2 text-sm hover:bg-[var(--md-sys-color-surface-variant)]" style={{ color: 'var(--md-sys-color-error)' }}>
              <LogOut size={16} /> Выйти
            </button>
          </div>
        </div>
      </nav>
    </header>
  );
};

export default UserHeader;