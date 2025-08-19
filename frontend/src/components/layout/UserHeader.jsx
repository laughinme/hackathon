import React, { useState, useContext, useEffect, useRef } from 'react';
import { Book, Map, Mail, User, LogOut, Plus, Search, Heart, Edit } from 'lucide-react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import AuthContext from '../../context/AuthContext';

const UserHeader = () => {
  const { currentUser, logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState(searchParams.get('query') || '');
  const menuRef = useRef(null);
  
  const handleSearchSubmit = (e) => {
    e.preventDefault();
    const trimmedQuery = searchQuery.trim();
    if (trimmedQuery) {
      navigate(`/home?query=${encodeURIComponent(trimmedQuery)}`);
    } else {
      navigate('/home');
    }
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setIsMenuOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [menuRef]);

  return (
    <header className="w-full p-4 flex items-center justify-between gap-4 sticky top-0 z-40" style={{ backgroundColor: 'var(--md-sys-color-surface-container-low)' }}>
      <Link to="/home" className="flex items-center flex-shrink-0">
        <Book size={28} style={{ color: 'var(--md-sys-color-primary)' }} />
        <span className="text-xl font-bold ml-2 hidden md:inline" style={{ color: 'var(--md-sys-color-on-surface)' }}>BOOK-EXCHANGE</span>
      </Link>

      <form onSubmit={handleSearchSubmit} className="flex-grow max-w-lg mx-4 relative">
        <input
          type="search"
          placeholder="Найти книгу по названию..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full p-2 pl-10 rounded-lg border-2 border-transparent focus:outline-none focus:border-[var(--md-sys-color-primary)] transition-colors"
          style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)'}}
        />
        <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2" style={{ color: 'var(--md-sys-color-on-surface-variant)'}} />
      </form>

      <nav className="flex items-center ml-auto space-x-2 md:space-x-4 flex-shrink-0">
        <button 
          onClick={() => navigate('/add-book')} 
          className="hidden sm:flex items-center px-3 py-2 rounded-lg font-semibold text-sm gap-2" 
          style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
          <Plus size={16}/> Добавить книгу
        </button>

        <Link to="/map" title="Карта точек" className="p-2 rounded-full hover:bg-[var(--md-sys-color-surface-container-high)]">
          <Map size={22} style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
        </Link>
        
        <Link to="/my-exchanges" title="Мои обмены" className="p-2 rounded-full hover:bg-[var(--md-sys-color-surface-container-high)]">
          <Mail size={22} style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
        </Link>

        <div className="relative" ref={menuRef}>
            <button onClick={() => setIsMenuOpen(prev => !prev)}>
                <img 
                    src={currentUser?.avatar_url || `https://placehold.co/80x80/DBC66E/3A3000?text=${currentUser?.username?.[0] || '?'}`} 
                    alt="User Avatar" 
                    className={`w-10 h-10 rounded-full object-cover cursor-pointer border-2 transition-colors ${isMenuOpen ? 'border-[var(--md-sys-color-primary)]' : 'border-transparent'}`}
                />
            </button>
            {isMenuOpen && (
              <div className="absolute right-0 mt-2 w-56 rounded-md shadow-lg py-1 z-50" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)'}}>
                <div className="px-4 py-2 border-b border-[var(--md-sys-color-outline-variant)]">
                    <p className="text-sm font-semibold truncate">{currentUser?.username}</p>
                    <p className="text-xs text-[var(--md-sys-color-on-surface-variant)] truncate">{currentUser?.email}</p>
                </div>
                <Link to="/profile" onClick={() => setIsMenuOpen(false)} className="flex w-full text-left items-center gap-3 px-4 py-2 text-sm hover:bg-[var(--md-sys-color-surface-variant)]">
                  <User size={16} /> Профиль
                </Link>
                <Link to="/profile/edit" onClick={() => setIsMenuOpen(false)} className="flex w-full text-left items-center gap-3 px-4 py-2 text-sm hover:bg-[var(--md-sys-color-surface-variant)]">
                  <Edit size={16} /> Редактировать профиль
                </Link>
                 <Link to="/liked-books" onClick={() => setIsMenuOpen(false)} className="flex w-full text-left items-center gap-3 px-4 py-2 text-sm hover:bg-[var(--md-sys-color-surface-variant)]">
                  <Heart size={16} /> Мои лайки
                </Link>
                <button onClick={logout} className="w-full text-left flex items-center gap-3 px-4 py-2 text-sm hover:bg-[var(--md-sys-color-surface-variant)]" style={{ color: 'var(--md-sys-color-error)' }}>
                  <LogOut size={16} /> Выйти
                </button>
              </div>
            )}
        </div>
      </nav>
    </header>
  );
};

export default UserHeader;