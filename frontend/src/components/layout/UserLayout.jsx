import React, { useState } from 'react';
import UserHeader from './UserHeader';
import HomePage from '../pages/HomePage';
import UserProfilePage from '../pages/UserProfilePage';

const UserLayout = () => {
  const [currentView, setCurrentView] = useState('catalog'); 

  const showProfile = () => setCurrentView('profile');
  const showCatalog = () => setCurrentView('catalog');

  return (
    <div className="flex flex-col min-h-screen">
      <UserHeader onProfileClick={showProfile} />
      <main className="flex-1 overflow-auto" style={{ backgroundColor: 'var(--md-sys-color-background)' }}>
        {currentView === 'profile' && <UserProfilePage onBack={showCatalog} />}
        {currentView === 'catalog' && <HomePage />}
      </main>
    </div>
  );
};

export default UserLayout;
