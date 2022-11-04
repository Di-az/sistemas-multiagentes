from mesa import Agent, Model
from mesa.space import Grid
from mesa.time import RandomActivation
from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer

class Ghost(Agent):
    def __init__(self, model, pos, matrix):
        super().__init__(model.next_id(), model)
        self.pos = pos
        self.matrix = matrix

    def step(self):
        print("POS: ", self.pos)
        
        next_moves = self.model.grid.get_neighborhood(self.pos, moore=False)  # moore es que son las 8 celdas colindantes / false es un newman -> solo 4 direcciones
        next_moves_copy = []

        print("NEXT: ", next_moves)
        for (x,y) in next_moves:
            if self.matrix[y][x] == 1:
                next_moves_copy.append((x,y))
        
        print("NEXT: ", next_moves_copy)
        print("-------------------")

        next_move = self.random.choice(next_moves_copy)
        self.model.grid.move_agent(self, next_move)

class WallBlock(Agent):
    def __init__(self, model, pos):
        super().__init__(model.next_id(), model)
        self.pos = pos


class Maze(Model):
    def __init__(self):
        super().__init__()
        self.schedule = RandomActivation(self)
        self.grid = Grid(17, 14, torus=False) # no usar la teletransportacion

        matrix = [
        [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
        [0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0],
        [0,1,0,1,0,0,0,1,1,1,0,1,0,1,0,1,0],
        [0,1,1,1,0,1,0,0,0,0,0,1,0,1,1,1,0],
        [0,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0],
        [0,1,0,1,0,1,0,0,0,0,0,1,1,1,0,1,0],
        [0,1,1,1,0,1,0,1,1,1,0,1,0,1,0,1,0],
        [0,1,0,1,0,1,0,1,1,1,0,1,0,1,0,1,0],
        [0,1,0,1,1,1,0,0,1,0,0,1,0,1,1,1,0],
        [0,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0],
        [0,1,1,1,0,1,0,0,0,0,0,1,0,1,1,1,0],
        [0,1,0,1,0,1,0,1,1,1,0,0,0,1,0,1,0],
        [0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0],
        [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        ]

        ghost = Ghost(self, (1,3), matrix)  # instanciando un agente ghost
        self.grid.place_agent(ghost, ghost.pos) # ponerlo en el grid
        self.schedule.add(ghost)  # agregarlo al scheduler

        for _,x,y in self.grid.coord_iter():
            if matrix[y][x] == 0:
                wallB = WallBlock(self, (x,y))
                self.grid.place_agent(wallB, wallB.pos) # ponerlo en el grid
                # print(f"block at ({x},{y})")
        
    def step(self):
        self.schedule.step()

# agregar interfaz de usuario
def agent_portrayal(agent):
    if isinstance(agent, Ghost):
        return {"Shape": "ghost.png", "Layer": 0}
    if isinstance(agent, WallBlock):
        return {"Shape": "rect", "w": 1, "h": 1, "Filled": "true", "Color": "Gray", "Layer": 1}



grid = CanvasGrid(agent_portrayal, 17, 14, 450, 450)

server = ModularServer(Maze, [grid], "PacMan", {})
server.port = 8000
server.launch()