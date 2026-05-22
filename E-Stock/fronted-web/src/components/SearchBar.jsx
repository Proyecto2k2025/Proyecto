import React from 'react';
import "../assets/styles/index.css";

function SearchBar({ searchTerm, onSearchChange, placeholder = "Buscar" }) {
  return (

    <div className="search-container">
      <label htmlFor="search-input" style={{display: 'none'}}>
        {placeholder}
      </label>
      
    
      <input
        id="search-input"
        type="text"
        placeholder={placeholder}
        value={searchTerm}
        onChange={(e) => onSearchChange(e.target.value)}
        aria-label={placeholder}
      />
    </div>
  );
}

export default SearchBar;