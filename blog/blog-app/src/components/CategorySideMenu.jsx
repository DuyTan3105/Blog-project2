import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { ListGroup, ListGroupItem } from 'reactstrap';
import { loadAllCategories } from '../services/category-service';

const CategorySideMenu = () => {
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await loadAllCategories();
                console.log('loading categories');
                console.log(data);
                setCategories([...data]);
            } catch (error) {
                console.log(error);
                toast.error('Error in loading categories');
            }
        };

        fetchData();
    }, []);

    return (
        <div style={{ width: '200px', margin: '0 auto' }}>
        <ListGroup>
            <ListGroupItem tag={Link} to="/" action className="border-0" style={{ backgroundColor: '#f8f9fa',fontSize:"25px",fontFamily:"sans-serif",fontWeight:"bold" }}>
                All Topics
            </ListGroupItem>
            {categories &&
                categories.map((cat, index) => (
                    <ListGroupItem
                        tag={Link}
                        to={`/categories/${cat.categoryId}`}
                        className="border-0 shadow-0 mt-1 italic"
                        key={index}
                        action
                 style={{ backgroundColor: '#f8f9fa', color: 'red', fontWeight: 'bold',fontSize:'20px'}} 
                    >
                        {cat.categoryTitle}
                    </ListGroupItem>
                ))}
        </ListGroup>
    </div>
    );
};

export default CategorySideMenu;