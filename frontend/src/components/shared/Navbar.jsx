import React from "react";
import { Link } from "react-router-dom";

const Navbar = ({ fullName }) => {
  return (
    <nav className="bg-blue-600 text-white px-6 py-4 flex justify-between items-center shadow">
      <h1 className="text-xl font-semibold">🎓 Student Clearance System</h1>
      <div className="flex items-center space-x-4">
        <span className="font-medium">{fullName}</span>
        <Link to="/notifications" className="underline hover:text-yellow-300">
          Notifications
        </Link>
        <a href="/logout" className="hover:underline text-red-300">
          Logout
        </a>
      </div>
    </nav>
  );
};

export default Navbar;
