import React from 'react';

const StatCard = ({ title, value, change }) => (
  <div className="p-4 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)' }}>
    <p className="text-sm font-medium" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{title}</p>
    <p className="text-3xl font-bold my-1" style={{ color: 'var(--md-sys-color-on-surface)' }}>{value}</p>
    <p className="text-sm" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{change}</p>
  </div>
);

export default StatCard;