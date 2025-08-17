import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { X, Send } from 'lucide-react';
import { reserveBook } from '../../api/services';

export default function ReserveBookModal({ book, onClose, onSuccess }) {
  const { register, handleSubmit, formState: { isSubmitting } } = useForm();
  const [error, setError] = useState(null);

  const onSubmit = async (data) => {
    setError(null);
    try {
      await reserveBook(book.id, { comment: data.comment });
      onSuccess();
      onClose();
    } catch (err) {
      console.error("Failed to reserve book:", err);
      setError(err.response?.data?.detail || "Не удалось забронировать книгу. Возможно, она уже недоступна.");
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50 p-4">
      <div className="p-6 rounded-xl w-full max-w-md relative" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
        <button onClick={onClose} className="absolute top-3 right-3 p-1 rounded-full hover:bg-[var(--md-sys-color-surface-variant)]">
          <X size={20} />
        </button>
        <h2 className="text-xl font-bold mb-2">Бронирование книги</h2>
        <p className="text-sm mb-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Вы собираетесь забронировать "{book.title}"</p>
        <form onSubmit={handleSubmit(onSubmit)}>
          <textarea
            {...register('comment')}
            rows="3"
            placeholder="Вы можете оставить комментарий для владельца (необязательно)"
            className="w-full p-2 rounded-lg border bg-transparent focus:outline-none focus:ring-2 border-[var(--md-sys-color-outline)]"
          />
          {error && <p className="text-sm text-red-400 mt-2">{error}</p>}
          <div className="flex justify-end gap-4 mt-4">
            <button type="button" onClick={onClose} className="py-2 px-4 rounded-lg font-semibold" style={{backgroundColor: 'var(--md-sys-color-surface-container-high)'}}>Отмена</button>
            <button type="submit" disabled={isSubmitting} className="py-2 px-4 rounded-lg font-semibold flex items-center gap-2" style={{backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)'}}>
              {isSubmitting ? 'Отправка...' : <><Send size={16}/> Подтвердить</>}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}