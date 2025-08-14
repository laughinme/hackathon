import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import BookCard from '../common/BookCard';
import BookFilters from '../common/BookFilters';
import ReserveBookModal from '../common/ReserveBookModal';
import { getBooksForYou } from '../../api/services';

const HomePage = () => {
  const navigate = useNavigate();
  const [books, setBooks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({ query: '' });

  const [reservingBook, setReservingBook] = useState(null);

  const fetchBooks = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await getBooksForYou(filters);
      setBooks(response.data);
    } catch (err) {
      console.error("Failed to fetch books", err);
      setError("Не удалось загрузить книги. Попробуйте позже.");
    } finally {
      setIsLoading(false);
    }
  }, [filters]);

  useEffect(() => {
    fetchBooks();
  }, [fetchBooks]);
  
  const handleReservationSuccess = () => {
     alert("Книга успешно забронирована!");
     fetchBooks(); 
  }
  
  const selectedBookForModal = books.find(b => b.id === reservingBook);

  const renderContent = () => {
    if (isLoading) {
      return <div className="p-8 text-center">Загрузка книг...</div>;
    }
    if (error) {
      return <div className="p-8 text-center text-red-400">{error}</div>;
    }
    if (books.length === 0) {
      return (
        <div className="text-center py-16 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
          <h3 className="text-lg font-semibold">Книги не найдены</h3>
          <p style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Попробуйте изменить фильтры или добавить свою книгу</p>
        </div>
      );
    }
    return (
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
        {books.map(book => (
          <BookCard 
            key={book.id} 
            book={book} 
            onReserve={() => setReservingBook(book.id)} 
            onSelect={() => navigate(`/book/${book.id}`)} 
          />
        ))}
      </div>
    );
  };

  return (
    <>
      {selectedBookForModal && (
        <ReserveBookModal
          book={selectedBookForModal}
          onClose={() => setReservingBook(null)}
          onSuccess={handleReservationSuccess}
        />
      )}
      <div className="p-4 md:p-8">
        <div className="flex justify-between items-center mb-4">
          <div>
            <h2 className="text-2xl font-bold">Для вас</h2>
            <p style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
              Рекомендации на основе ваших предпочтений
            </p>
          </div>
        </div>

        <BookFilters onFilterChange={setFilters} />
        
        {renderContent()}
      </div>
    </>
  );
};

export default HomePage;