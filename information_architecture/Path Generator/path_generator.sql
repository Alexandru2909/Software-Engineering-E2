select * from nodes;
select* from edges;
describe edges;

create or replace package shortest_path is
    cursor curs_node(v_id nodes.id%type) is select id2,cost from edges where id1=v_id;
    type adj_node is table of curs_node%rowtype index by pls_integer;
    type adj_node_list is table of adj_node index by pls_integer;
    adj_list adj_node_list;
    heap_size number(38);
    heap_capacity number(38);
    type array is table of number(38) index by pls_integer;
    pos array;
    type array_float is table of float index by pls_integer;
    dist array_float;
    
    cursor curs_heap is select id1, cost from edges;
    type heap is table of curs_heap%rowtype index by pls_integer;
    min_heap heap;
    root curs_heap%rowtype;
    last_node curs_heap%rowtype;
    
    procedure create_adj_list;
    procedure minHeapify(idx number);
    function extract_min return edges.id1%type;
    function is_in_min_heap(v edges.id1%type) return boolean;
    procedure decrease_key(v2 number, dist2 number);
    procedure dijkstra(src number, dest number);
   
end shortest_path;

create or replace package body shortest_path is
    v number;
    v_id nodes.id%type;
    v_contor nodes.id%type;
    smallest number(38);
    left_node number(38);
    right_node number(38);
    aux_id edges.id1%type;
    aux_cost edges.cost%type;
    aux number(38);
    u edges.id1%type;
    
    procedure create_adj_list is
    begin
      select count(id) into v from nodes;
      for v_id in 1..v loop
          v_contor:=1;
          open curs_node(v_id);
          loop
            fetch curs_node into adj_list(v_id)(v_contor).id2,adj_list(v_id)(v_contor).cost;
            exit when curs_node%NOTFOUND;
            v_contor:=v_contor+1;
          end loop;
          close curs_node;
      end loop;
    end create_adj_list;
    
    procedure minHeapify(idx number) is
    begin
      smallest:=idx;
      left_node:=2*idx+1;
      right_node:=2*idx+2;
      if(left_node<heap_size and dist(left_node)<dist(smallest)) then
        smallest:=left_node;
      end if;
      if(right_node<heap_size and dist(right_node)<dist(smallest)) then
        smallest:=right_node;
      end if;
      if(smallest<>idx) then
        --swapping positions
        pos(min_heap(smallest).id1):=idx;
        pos(min_heap(idx).id1):=smallest;
        --swapping the 2 nodes
        aux_id:=min_heap(smallest).id1;
        aux_cost:=min_heap(smallest).cost;
        min_heap(smallest).id1:=min_heap(idx).id1;
        min_heap(smallest).cost:=min_heap(idx).cost;
        min_heap(idx).id1:=aux_id;
        min_heap(idx).cost:=aux_cost;
        minHeapify(smallest);
      end if;
    end minHeapify;
    
    function extract_min return edges.id1%type as
    begin
      root.id1:=min_heap(1).id1;
      root.cost:=min_heap(1).cost;
      last_node.id1:=min_heap(heap_size).id1;
      last_node.cost:=min_heap(heap_size).cost;
      min_heap(1):=last_node;
      
      pos(root.id1):=heap_size-1;
      pos(last_node.id1):=1;
      
      heap_size:=heap_size-1;
      minHeapify(1);
      return root.id1;
    end extract_min;
    
    procedure decrease_key(v2 number, dist2 number) is
    begin
        aux:=pos(v2);
        min_heap(aux).cost:=dist2;
        while(aux!=1 and min_heap(aux).cost<min_heap((aux-1)/2).cost) loop
            pos(min_heap(aux).id1):=(aux-1)/2;
            pos(min_heap((aux-1)/2).id1):=aux;
            --swapping the node with its parent
            aux_id:=min_heap(aux).id1;
            aux_cost:=min_heap(aux).cost;
            min_heap(aux).id1:=min_heap((aux-1)/2).id1;
            min_heap(aux).cost:=min_heap((aux-1)/2).cost;
            min_heap((aux-1)/2).id1:=aux_id;
            min_heap((aux-1)/2).cost:=aux_cost;
            aux:=(aux-1)/2;
        end loop;
    end decrease_key;
    
     function is_in_min_heap(v edges.id1%type) return boolean as
     begin
      if(pos(v)<=heap_size) then
        return true;
      end if;
      return false;
     end is_in_min_heap;
    
    procedure dijkstra(src number, dest number) is
    begin
      
      create_adj_list;
      select count(*) into v from nodes;
      heap_capacity:=v;
      heap_size:=0;
      for aux in 1..v loop
        dist(aux):=200;
        pos(aux):=aux;
        min_heap(aux).id1:=aux;
        min_heap(aux).cost:=dist(aux);
      end loop;
    
    min_heap(src).id1:=src;
    min_heap(src).cost:=dist(src);
    pos(src):=src;
    dist(src):=0;

    decrease_key(src,dist(src));
    
    heap_size:=v;
     
     while(heap_size!=0) loop
        u:=extract_min;
        for v in adj_list(u).first..adj_list(u).last loop
          dbms_output.put_line('nodes '||u||' '||v||' dist '||dist(u)||' '||dist(v));
          if(is_in_min_heap(v)) then
            dbms_output.put_line('should change');
            end if;
            if(dist(u)!=200) then
            dbms_output.put_line('should change3');
            end if;
            if(adj_list(u)(v).cost+dist(u)<dist(v)) then
            dbms_output.put_line('should change4');
            end if;
          if(is_in_min_heap(v) and dist(u)!=200 and adj_list(u)(v).cost+dist(u)<dist(v)) then
            dbms_output.put_line('should change2');
            dist(v):=dist(u)+adj_list(u)(v).cost;
            decrease_key(v,dist(v));
            end if;
        end loop;
     end loop;
    for v in 1..dist.count loop                     
      dbms_output.put_line(v||' dist '||dist(v));
      end loop;
    end dijkstra;
end shortest_path;


begin
  shortest_path.dijkstra(1,1);
end;