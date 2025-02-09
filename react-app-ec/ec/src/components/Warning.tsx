import React from 'react'
import './Warning.css'

const Warning = ({ title, message, confirmar, cancelar, onConfirm, onCancel }) => {
  return (
    <div className="dialog-overlay">
      <div className="dialog-box">
        <div className="dialog-header">
          <div className="icon-container">
            <svg
              aria-hidden="true"
              stroke="currentColor"
              strokeWidth="1.5"
              viewBox="0 0 24 24"
              fill="none"
              className="icon"
            >
              <path
                d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"
                strokeLinejoin="round"
                strokeLinecap="round"
              ></path>
            </svg>
          </div>
          <h3 className="dialog-title">{title}</h3>
        </div>
        <div className="dialog-body">
          <p className="dialog-message">{message}</p>
        </div>
        <div className="dialog-footer">
          <button
            className="confirm-button"
            onClick={() => onConfirm(true)}
          >
            {confirmar}
          </button>
          <button
            className="cancel-button"
            onClick={() => onCancel(false)}
          >
            {cancelar }
          </button>
        </div>
      </div>
    </div>
  );
};

export default Warning;
