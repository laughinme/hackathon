import React, { useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, LayoutGrid, List } from 'lucide-react';
import BookCard from '../common/BookCard';
import BookFilters from '../common/BookFilters';
import BookDetailPage from './BookDetailPage';

const HomePage = ({ books }) => {
  const navigate = useNavigate();
  const [view, setView] = useState({ name: 'catalog', bookId: null });
  const [filters, setFilters] = useState({ sort: 'newest', genre: 'all', distance: 50, rating: 1 });

  const handleReserveBook = (id) => {
    alert(`Демо: Резервирование книги ${id}. Логика должна быть в App.jsx для глобального эффекта.`);
  };

  const processedBooks = useMemo(() => {
    let filtered = books
      .filter(book => filters.genre === 'all' || book.genre === filters.genre)
      .filter(book => book.distanceNum <= filters.distance)
      .filter(book => book.rating >= filters.rating);

    switch (filters.sort) {
      case 'rating':
        filtered.sort((a, b) => b.rating - a.rating);
        break;
      case 'distance':
        filtered.sort((a, b) => a.distanceNum - b.distanceNum);
        break;
      case 'newest':
      default:
        filtered.sort((a, b) => b.id - a.id);
        break;
    }
    return filtered;
  }, [books, filters]);

  if (view.name === 'detail') {
    const selectedBook = books.find(b => b.id === view.bookId);
    return <BookDetailPage book={selectedBook} onBack={() => setView({ name: 'catalog' })} />;
  }

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-4">
        <div>
          <h2 className="text-2xl font-bold">Каталог книг</h2>
          <p style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
            Найдено {processedBooks.length} книг
          </p>
        </div>
        <div className="flex items-center space-x-2">
          <button onClick={() => navigate('/add-book')} className="flex items-center px-4 py-2 rounded-lg font-semibold" style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
            <Plus size={20} className="mr-2" /> Добавить книгу
          </button>
          <div className="flex items-center rounded-lg" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)' }}>
            <button className="p-2 rounded-l-lg" style={{ backgroundColor: 'var(--md-sys-color-primary-container)', color: 'var(--md-sys-color-on-primary-container)' }}><LayoutGrid size={20} /></button>
            <button className="p-2 rounded-r-lg"><List size={20} /></button>
          </div>
        </div>
      </div>

      <BookFilters onFilterChange={setFilters} />

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {processedBooks.map(book => (
          <BookCard 
            key={book.id} 
            book={book} 
            onReserve={() => handleReserveBook(book.id)} 
            onSelect={() => setView({ name: 'detail', bookId: book.id })} 
          />
        ))}
      </div>
    </div>
  );
};

export default HomePage;