import React, { useState, useMemo } from 'react';
import { Plus, LayoutGrid, List } from 'lucide-react';
import AddBookPage from './AddBookPage';
import BookCard from '../common/BookCard';
import BookFilters from '../common/BookFilters';
import BookDetailPage from './BookDetailPage';

const MOCK_BOOKS = [
    { id: 1, title: 'Война и мир', author: 'Лев Толстой', rating: 4.8, genre: 'Классика', distanceNum: 0.8, distance: '0.8 км', owner: { name: 'Анна К.', rating: 4.9, avatar: 'https://placehold.co/80x80/DBC66E/3A3000?text=A' }, condition: 'good', added: '2 ч. назад', status: 'available', image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Война+и+мир', tags: ['Классика', 'История'], publisher: 'Эксмо', year: 2019, description: 'Величайший роман всех времен и народов, эпическая картина русской жизни начала XIX века.' },
    { id: 2, title: 'Гарри Поттер и философский камень', author: 'Дж. К. Роулинг', rating: 4.9, genre: 'Фэнтези', distanceNum: 1.2, distance: '1.2 км', owner: { name: 'Михаил П.', rating: 4.7, avatar: null }, condition: 'new', added: '2 ч. назад', status: 'available', image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Гарри+Поттер', tags: ['Фэнтези', 'Приключения'], publisher: 'Росмэн', year: 2000, description: 'Приключения юного волшебника Гарри Поттера и его друзей в школе чародейства и волшебства Хогвартс.' },
    { id: 3, title: 'Мастер и Маргарита', author: 'Михаил Булгаков', rating: 4.7, genre: 'Классика', distanceNum: 2.5, distance: '2.5 км', owner: { name: 'Елена С.', rating: 4.8, avatar: null }, condition: 'good', added: '2 ч. назад', status: 'reserved', image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Мастер+и+Маргарита', tags: ['Мистика', 'Сатира'], publisher: 'АСТ', year: 2015, description: 'Захватывающая история о визите дьявола в Москву 1930-х годов, переплетенная с историей Понтия Пилата.' },
    { id: 4, title: 'Атлант расправил плечи', author: 'Айн Рэнд', rating: 4.6, genre: 'Философия', distanceNum: 3.1, distance: '3.1 км', owner: { name: 'Дмитрий В.', rating: 5.0, avatar: null }, condition: 'good', added: '5 ч. назад', status: 'available', image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Атлант', tags: ['Философия', 'Антиутопия'], publisher: 'Альпина', year: 2021, description: 'Роман-антиутопия, в котором ключевые фигуры американского бизнеса объявляют забастовку и исчезают, оставляя страну в хаосе.' },
];

const HomePage = () => {
  const [view, setView] = useState({ name: 'catalog', bookId: null });
  
  const [books, setBooks] = useState(MOCK_BOOKS);
  const [filters, setFilters] = useState({ sort: 'newest', genre: 'all', distance: 50, rating: 1 });

  const handleReserveBook = (id) => {
    setBooks(prevBooks => prevBooks.map(book => book.id === id ? { ...book, status: 'reserved' } : book));
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

  if (view.name === 'add') {
    return <AddBookPage onBack={() => setView({ name: 'catalog' })} />;
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
          <button onClick={() => setView({ name: 'add' })} className="flex items-center px-4 py-2 rounded-lg font-semibold" style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
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
            onReserve={handleReserveBook} 
            onSelect={() => setView({ name: 'detail', bookId: book.id })} 
          />
        ))}
      </div>
    </div>
  );
};

export default HomePage;
